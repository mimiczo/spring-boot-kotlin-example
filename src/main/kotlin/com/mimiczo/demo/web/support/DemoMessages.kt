package com.mimiczo.demo.web.support

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Component

/**
 * com.mimiczo.demo.web.support
 * Created by j on 2017.07.31
 */
@Component
class DemoMessages @Autowired constructor(val messageSource: MessageSource) {

    operator fun get(code: String): String = messageSource.getMessage(code, null, LocaleContextHolder.getLocale())
    operator fun get(code: String, fields: Array<out String>): String = messageSource.getMessage(code, fields, LocaleContextHolder.getLocale())
}