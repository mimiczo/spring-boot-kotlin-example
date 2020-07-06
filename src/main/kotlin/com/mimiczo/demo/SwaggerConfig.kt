package com.mimiczo.demo

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Pageable
import springfox.documentation.builders.ParameterBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.schema.ModelRef
import springfox.documentation.service.ApiKey
import springfox.documentation.service.AuthorizationScope
import springfox.documentation.service.SecurityReference
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger.web.ApiKeyVehicle
import springfox.documentation.swagger.web.SecurityConfiguration
import springfox.documentation.swagger2.annotations.EnableSwagger2

/**
 * com.mimiczo.demo
 * Created by j on 2017.07.28
 */
@Configuration
@EnableSwagger2
class SwaggerConfig {

    @Value("\${demo.name}")
    val applicationName: String = ""
    @Value("\${demo.jwt.headerName}")
    val headerNameOfJwtToken: String = ""
    @Value("\${demo.basePackage}")
    val basePackage: String = ""

    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .groupName(applicationName)
                .securitySchemes(arrayListOf(apiKey()))
                .securityContexts(arrayListOf(securityContext()))
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .paths(PathSelectors.any()).build()
                .useDefaultResponseMessages(false)
                .ignoredParameterTypes(Pageable::class.java)
                .globalOperationParameters(
                        arrayListOf(
                                ParameterBuilder()
                                .name("Accept-Language")
                                .description("language of header")
                                .modelRef(ModelRef("string"))
                                .parameterType("header")
                                .required(false)
                                .build()
                        )
                )
    }

    @Bean
    fun security() = SecurityConfiguration("demo-id", "demo-secret", "demo-realm", "demo-app"
                                            , headerNameOfJwtToken, ApiKeyVehicle.HEADER, "api_key", ",")

    @Bean
    fun apiKey() = ApiKey(headerNameOfJwtToken, headerNameOfJwtToken, "header")

    internal fun securityContext() = SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.ant("/me/**")).build()

    internal fun defaultAuth() = arrayListOf(SecurityReference(headerNameOfJwtToken, arrayOf(AuthorizationScope("global", "accessEverything"))))
}