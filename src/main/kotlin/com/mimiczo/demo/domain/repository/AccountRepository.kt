package com.mimiczo.demo.domain.repository

import com.mimiczo.demo.domain.Account
import org.springframework.data.jpa.repository.JpaRepository

/**
 * com.mimiczo.demo.domain.repository
 * Created by j on 2017.07.31
 */
interface AccountRepository : JpaRepository<Account, Long> {

    fun findByEmail(email: String): Account?
}