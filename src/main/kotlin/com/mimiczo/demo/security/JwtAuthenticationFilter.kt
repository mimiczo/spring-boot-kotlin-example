package com.mimiczo.demo.security

import com.mimiczo.demo.web.dto.ErrorDto
import com.mimiczo.demo.web.support.DemoMessages
import com.mimiczo.demo.web.support.ResponseCode
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.http.server.ServletServerHttpResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


/**
 * com.mimiczo.demo.security
 * Created by j on 2017.08.02
 */
class JwtAuthenticationFilter(val accountService: UserDetailsService, val demoMessages: DemoMessages): OncePerRequestFilter() {

    @Value("\${demo.jwt.headerName}")
    val headerNameOfJwtToken: String = ""

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val token = request.getHeader(headerNameOfJwtToken)
        val authResult: Authentication?

        try {
            var userDetails = accountService.loadUserByUsername(token)
            if(userDetails == null) {
                response.status = HttpStatus.UNAUTHORIZED.value()
                this.errorResponse(ResponseCode.UNAUTHORIZED, demoMessages[ResponseCode.UNAUTHORIZED.name], response)
                return
            }
            authResult = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
            authResult.details = WebAuthenticationDetailsSource().buildDetails(request)
            SecurityContextHolder.getContext().authentication = authResult
        } catch (failed: AuthenticationException) {
            // Authentication failed
            this.errorResponse(ResponseCode.UNAUTHORIZED, demoMessages[ResponseCode.UNAUTHORIZED.name], response)
            return
        } catch (failed: RuntimeException) {
            // Authentication failed
            this.errorResponse(ResponseCode.UNAUTHORIZED, demoMessages[ResponseCode.UNAUTHORIZED.name], response)
            return
        }

        filterChain.doFilter(request, response)
    }

    private fun errorResponse(errorCode: ResponseCode, message: String, response: HttpServletResponse)
            = MappingJackson2HttpMessageConverter().write(ErrorDto.of(HttpStatus.UNAUTHORIZED, errorCode.name, message), MediaType.APPLICATION_JSON, ServletServerHttpResponse(response))
}