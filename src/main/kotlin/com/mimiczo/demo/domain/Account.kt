package com.mimiczo.demo.domain

import com.mimiczo.demo.service.PasswordService
import com.mimiczo.demo.web.dto.AccountParam
import com.mimiczo.demo.web.dto.MeParam
import java.util.*
import javax.persistence.*

/**
 * com.mimiczo.demo.domain
 * Created by j on 2017.07.28
 */
@Entity
data class Account constructor(@Column(unique = true, updatable = false) var email: String
                   , var password: String
                   , var name: String
                   , var nickname: String
                   , @Column(columnDefinition = "varchar(500)") var token: String
                   , val createdAt: Date = Date()) {

    constructor(param: AccountParam, token: String, passwordService: PasswordService): this(param.email, passwordService.encode(param.password), param.name, param.nickname, token, Date())

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0
    var updatedAt: Date = createdAt.clone() as Date

    fun signin(token: String) {
        this.token = token
        this.updatedAt = Date()
    }

    fun modify(param: MeParam) {
        this.name = param.name
        this.nickname = param.nickname
        this.updatedAt = Date()
    }
}