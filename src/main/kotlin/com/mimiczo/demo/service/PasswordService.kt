package com.mimiczo.demo.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

/**
 * com.mimiczo.demo.service
 * Created by j on 2017.07.31
 */
@Service
class PasswordService(val passwordEncoder: PasswordEncoder) {

    fun encode(rawPassword: String) = passwordEncoder.encode(rawPassword)
    fun matches(rawPassword: String, password: String) = passwordEncoder.matches(rawPassword, password)
}