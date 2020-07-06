package com.mimiczo.demo.web.support

/**
 * com.mimiczo.demo.web.support
 * Created by j on 2017.07.31
 */
enum class ResponseCode private constructor(val code: Int) {

    SUCCESS(200)
    , DEFAULT_ERROR(400)
    , PARAMETER_ERROR(400)
    , DATA_NOTFOUND(400)
    , VERIFY_FAILED_PASSWORD(400)
    , DUPLICATE_ACCOUNT(400)
    , SYSTEM_ERROR(500)
    , UNAUTHORIZED(401);

    val stringCode = this.code.toString()

    companion object {
        fun parse(value: Int): ResponseCode {
            ResponseCode.values().filter{ it.code == value }.forEach { return it }
            throw IllegalArgumentException("Unknown " + value)
        }
    }
}