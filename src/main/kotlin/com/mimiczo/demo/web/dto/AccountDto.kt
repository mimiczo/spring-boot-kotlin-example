package com.mimiczo.demo.web.dto

import com.mimiczo.demo.domain.Account
import java.util.*

/**
 * com.mimiczo.demo.web.dto
 * Created by j on 2017.07.28
 */
data class AccountDto(var email: String
                      , var name: String
                      , var nickname: String
                      , var token: String
                      , var createdAt: Date?
                      , var updatedAt: Date?) {


    companion object {
        fun of(domain: Account): AccountDto {
            return AccountDto(domain.email, domain.name, domain.nickname, domain.token, domain.createdAt, domain.updatedAt)
        }
    }
}