package com.mimiczo.demo.web.dto

import org.hibernate.validator.constraints.Email
import org.hibernate.validator.constraints.NotEmpty
import javax.validation.constraints.Size

/**
 * com.mimiczo.demo.web.dto
 * Created by j on 2017.07.28
 */
data class AccountParam (

    @field:NotEmpty(groups = arrayOf(Signin::class))
    @field:Email(groups = arrayOf(Signin::class))
    var email: String = "",

    @field:NotEmpty(groups = arrayOf(Signin::class))
    @field:Size(groups = arrayOf(Signin::class), min = 5, max = 20)
    var password: String = "",

    @field:NotEmpty(groups = arrayOf(Join::class))
    @field:Size(groups = arrayOf(Join::class), min = 2, max = 50)
    var name: String = "",

    @field:Size(groups = arrayOf(Join::class), min = 0, max = 50)
    var nickname: String = ""
) {
    interface Join
    interface Signin
}

data class MeParam (
    var id: Long = 0,

    @field:NotEmpty
    @field:Size(min = 2, max = 50)
    var name: String = "",

    @field:NotEmpty
    @field:Size(max = 50)
    var nickname: String = ""
)