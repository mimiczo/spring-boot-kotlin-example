package com.mimiczo.demo.web.support

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User


/**
 * com.mimiczo.demo.web.support
 * Created by j on 2017.08.01
 */
class DemoSession {

    companion object {
        internal fun getUserDetails(): User? {
            var session: User? = null
            if (SecurityContextHolder.getContext().authentication != null) {
                session = SecurityContextHolder.getContext().authentication.principal as User
            }
            return session
        }

        fun getId(): Long = getUserDetails()!!.username.toLong()
    }
}