package com.yourssu.assignmentblog.global.common.config

import com.yourssu.assignmentblog.global.auth.filter.CustomLoginFilter
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springdoc.core.customizers.OpenApiCustomiser
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter

@Configuration
class SwaggerConfig(
    @Value("\${springdoc.version}")
    private val version: String,
) {
    @Bean
    fun openAPI(): OpenAPI {
        val info =
            Info()
                .title("YourSSU 인큐베이팅")
                .version(version)
                .description("YourSSU 인큐베이팅 과제 API 문서입니다.")

        val securityScheme =
            SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .`in`(SecurityScheme.In.HEADER)
                .name(HttpHeaders.AUTHORIZATION)

        val securityItem = SecurityRequirement()
        securityItem.addList("JWT")

        return OpenAPI()
            .components(Components().addSecuritySchemes("JWT", securityScheme))
            .addSecurityItem(securityItem)
            .info(info)
    }

    @Bean
    fun customLoginEndpointCustomizer(loginFilter: CustomLoginFilter): OpenApiCustomiser {
        return LoginEndpointCustomizer<AbstractAuthenticationProcessingFilter>()
            .loginEndpointCustomizer(loginFilter, "login")
    }
}
