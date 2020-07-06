package com.mimiczo.demo.web.support

/**
 * com.mimiczo.demo.web.support
 * Created by j on 2017.07.31
 */
class DemoException : RuntimeException {

    var code: Int = 0
    var fields: Array<out String>? = null

    constructor(code: Enum<*>) : super(code.name) {
        this.code = code.ordinal
    }

    constructor(code: Enum<*>, vararg fields: String) : super(code.name) {
        this.code = code.ordinal
        this.fields = fields
    }
}