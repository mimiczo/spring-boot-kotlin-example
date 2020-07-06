package com.mimiczo.demo

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

@SpringBootApplication
class Application {

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            SpringApplication.run(Application::class.java, *args)
        }
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun configurer(): WebMvcConfigurer {
        return object : WebMvcConfigurerAdapter() {
            override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
                registry.addResourceHandler("/static/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/")
                registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/")
            }
        }
    }
}