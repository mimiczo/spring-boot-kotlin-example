package com.mimiczo.demo.web.support

/**
 * com.mimiczo.demo.web.support
 * Created by j on 2017.07.31
 */
class DemoAssert {

    companion object {

        private fun assertBoolean(validation: Boolean, code: Enum<*>, field: String, vararg expressions: Boolean) = expressions.filter { it != validation }.forEach { throw DemoException(code, field) }

        fun isTrue(code: Enum<*>, field: String, vararg expressions: Boolean) {
            assertBoolean(true, code, field, *expressions)
        }

        fun isFalse(code: Enum<*>, field: String, vararg expressions: Boolean) {
            assertBoolean(false, code, field, *expressions)
        }

        fun Null(code: Enum<*>, field: String, vararg objects: Any?) = objects.filter { it != null }.forEach { throw DemoException(code, field) }
        fun notNull(code: Enum<*>, field: String, vararg objects: Any?) = objects.filter { it == null }.forEach { throw DemoException(code, field) }

        fun isEmpty(code: Enum<*>, field: String, vararg strs: String) = strs.filter { it.isNotEmpty() }.forEach { throw DemoException(code, field) }
        fun notEmpty(code: Enum<*>, field: String, vararg strs: String) = strs.filter { it.isEmpty() }.forEach { throw DemoException(code, field) }
    }
}