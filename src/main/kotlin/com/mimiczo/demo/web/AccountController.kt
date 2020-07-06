package com.mimiczo.demo.web

import com.mimiczo.demo.service.AccountService
import com.mimiczo.demo.web.dto.AccountDto
import com.mimiczo.demo.web.dto.AccountParam
import com.mimiczo.demo.web.support.DemoAssert
import com.mimiczo.demo.web.support.ResponseCode
import io.swagger.annotations.ApiOperation
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindException
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * com.mimiczo.demo.web
 * Created by j on 2017.07.28
 */
@RestController
@RequestMapping("/account")
class AccountController(val accountService: AccountService) {

    @ApiOperation(value = "계정생성"
            , notes = "사용자정보 반환"
            , response = AccountDto::class)
    @PostMapping("/join")
    fun postJoin(@Validated(*arrayOf(AccountParam.Join::class, AccountParam.Signin::class)) @RequestBody param: AccountParam, bindingResult: BindingResult): ResponseEntity<AccountDto> {
        if(bindingResult.hasErrors()) throw BindException(bindingResult)

        DemoAssert.Null(ResponseCode.DUPLICATE_ACCOUNT, "join", accountService.get(param.email))
        return ResponseEntity.ok(AccountDto.of(accountService.createAccount(param)))
    }

    @ApiOperation(value = "로그인"
            , notes = "사용자정보 반환"
            , response = AccountDto::class)
    @PostMapping("/signin")
    fun postSignin(@Validated(AccountParam.Signin::class) @RequestBody param: AccountParam, bindingResult: BindingResult): ResponseEntity<AccountDto> {
        if(bindingResult.hasErrors()) throw BindException(bindingResult)

        return ResponseEntity.ok(AccountDto.of(accountService.signin(param.email, param.password)))
    }
}