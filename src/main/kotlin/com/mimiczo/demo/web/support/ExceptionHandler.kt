package com.mimiczo.demo.web.support

import com.mimiczo.demo.web.dto.ErrorDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

/**
 * com.mimiczo.demo.web.support
 * Created by j on 2017.08.01
 */
@ControllerAdvice
class ExceptionHandler @Autowired constructor(var messages: DemoMessages) {

    @ExceptionHandler(DemoException::class)
    fun handleDemoException(exception: DemoException) = ResponseEntity(ErrorDto.of(HttpStatus.BAD_REQUEST, exception.message!!, messages[exception.message, exception.fields!!]), HttpStatus.BAD_REQUEST)

    @ExceptionHandler(BindException::class)
    fun handleBindException(exception: BindException) = ResponseEntity(ErrorDto.of(HttpStatus.BAD_REQUEST, ResponseCode.PARAMETER_ERROR.name, exception.fieldError.defaultMessage + " [" + exception.fieldError.field + "]"), HttpStatus.BAD_REQUEST)
}