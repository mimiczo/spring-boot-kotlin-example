package com.mimiczo.demo

import com.mimiczo.demo.security.JwtAuthenticationFilter
import com.mimiczo.demo.web.support.DemoMessages
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.servlet.http.HttpServletResponse

/**
 * com.mimiczo.demo
 * Created by j on 2017.07.31
 */
@Configuration
@EnableWebSecurity
class SecurityConfig @Autowired constructor(val accountService: UserDetailsService, val demoMessages: DemoMessages): WebSecurityConfigurerAdapter() {

    @Bean
    fun jwtAuthenticationFilter() = JwtAuthenticationFilter(accountService, demoMessages)

    @Throws(Exception::class)
    override fun configure(web: WebSecurity) {
        web.ignoring()
                .antMatchers(HttpMethod.GET, "/favicon.ico", "/static/**")
                .antMatchers("/v2/api-docs**", "/account/**", "/h2-console/**")
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
            .anyRequest().authenticated()
            .and()
                .addFilterAfter(jwtAuthenticationFilter(), BasicAuthenticationFilter::class.java)
                .sessionManagement().maximumSessions(1).and().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .exceptionHandling()
                .authenticationEntryPoint { _, response, _ -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized") }
                .accessDeniedHandler { _, response, _ -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized Denied") }
            .and()
            .csrf().disable()
            .httpBasic().and()
            .headers().cacheControl()
    }

    @Bean
    fun registration(jwtAuthenticationFilter: JwtAuthenticationFilter): FilterRegistrationBean<*> {
        val registration = FilterRegistrationBean(jwtAuthenticationFilter)
        registration.isEnabled = false
        return registration
    }
}