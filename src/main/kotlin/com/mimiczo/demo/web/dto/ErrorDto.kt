package com.mimiczo.demo.web.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.http.HttpStatus
import java.util.*

/**
 * com.mimiczo.demo.web.dto
 * Created by j on 2017.07.31
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
data class ErrorDto(val status:Int
                    , val messageCode:String
                    , val message:String
                    , val timestamp: Date) {

    companion object {
        fun of(httpStatus:HttpStatus, messageCode: String, message: String) = ErrorDto(httpStatus.value(), messageCode, message, Date())
    }
}