package com.mimiczo.demo.service

import com.mimiczo.demo.domain.Account
import com.mimiczo.demo.domain.repository.AccountRepository
import com.mimiczo.demo.web.dto.AccountParam
import com.mimiczo.demo.web.dto.MeParam
import com.mimiczo.demo.web.support.DemoAssert
import com.mimiczo.demo.web.support.DemoException
import com.mimiczo.demo.web.support.ResponseCode
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * com.mimiczo.demo
 * Created by j on 2017.07.31
 */
@Service
class AccountService @Autowired constructor(val passwordService: PasswordService, val accountRepository: AccountRepository): UserDetailsService {

    @Transactional(readOnly = true)
    fun get(id: Long) = accountRepository.getOne(id) ?: throw DemoException(ResponseCode.DATA_NOTFOUND)

    @Transactional(readOnly = true)
    fun get(email: String) = accountRepository.findByEmail(email)

    @Transactional(readOnly = true)
    fun signin(email: String, rawPassword: String): Account {
        this.get(email)?.let {
            DemoAssert.isTrue(ResponseCode.VERIFY_FAILED_PASSWORD, "signin", passwordService.matches(rawPassword, it.password))
            it.signin(generateToken(it.email, it.name))
            return saveAccount(it)
        } ?: throw DemoException(ResponseCode.DATA_NOTFOUND)
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    fun createAccount(param: AccountParam) = saveAccount(Account(param, generateToken(param.email, param.name), passwordService))

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    fun modifyAccount(param: MeParam) = this.get(param.id).apply {
        modify(param)
        return saveAccount(this)
    }

    internal fun saveAccount(account: Account) = accountRepository.save(account)

    internal fun generateToken(email: String, name: String): String {
        val claimsMap = HashMap<String, Any>()
        claimsMap.put("email", email)

        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .setClaims(Jwts.claims(claimsMap))
                .setSubject("accessToken")
                .setAudience(name)
                .setIssuedAt(Date())
                .setIssuer("demo-app")
                .setExpiration(Date(Date().time + TimeUnit.DAYS.toMillis(30)))
                .compact()
    }

    @Value("\${demo.jwt.secret}")
    val jwtSecret: String = ""

    @Transactional(readOnly = true)
    override fun loadUserByUsername(token: String): UserDetails {
        if(token.isNullOrEmpty()) throw UsernameNotFoundException("Null the userToken!")

        val claims = this.parseAndValidationJwt(jwtSecret, token)
        val email = claims["email"].toString()
        return Optional.ofNullable(accountRepository.findByEmail(email))
                .filter { it.token.equals(token) }
                .map {
                    account ->
                    User(account.id.toString(), account.email, AuthorityUtils.createAuthorityList("ROLE_USER"))
                }
                .orElseThrow{ UsernameNotFoundException("Not found the user ${email}!") }
    }

    internal fun parseAndValidationJwt(secret: String, token: String) = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).body ?: throw UsernameNotFoundException("Not found the userInfo")
}