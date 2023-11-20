package com.yourssu.assignmentblog.global.auth.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.yourssu.assignmentblog.domain.user.repository.UserRepository
import com.yourssu.assignmentblog.global.auth.filter.CustomLoginFilter
import com.yourssu.assignmentblog.global.auth.filter.JwtAuthenticationFilter
import com.yourssu.assignmentblog.global.auth.handler.LoginFailureHandler
import com.yourssu.assignmentblog.global.auth.handler.LoginSuccessHandler
import com.yourssu.assignmentblog.global.auth.jwt.token.TokenProvider
import com.yourssu.assignmentblog.global.auth.service.CustomUserDetailsService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.logout.LogoutFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    @Value("\${jwt.access.header}")
    private val accessTokenHeader: String,
    @Value("\${jwt.refresh.header}")
    private val refreshTokenHeader: String,
    private val customUserDetailsService: CustomUserDetailsService,
    private val loginSuccessHandler: LoginSuccessHandler,
    private val loginFailureHandler: LoginFailureHandler,
    private val tokenProvider: TokenProvider,
    private val userRepository: UserRepository,
) {
    companion object {
        val objectMapper = ObjectMapper()
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .formLogin()
            .disable()
            .httpBasic()
            .disable()
            .csrf()
            .disable()
            .headers()
            .frameOptions()
            .disable()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests()
            .anyRequest().permitAll()

        http.addFilterAfter(customLoginFilter(), LogoutFilter::class.java)
        http.addFilterBefore(jwtAuthenticationFilter(), CustomLoginFilter::class.java)

        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authenticationManager(): AuthenticationManager {
        val daoAuthenticationProvider = DaoAuthenticationProvider()

        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder())
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService)
        return ProviderManager(daoAuthenticationProvider)
    }

    @Bean
    fun customLoginFilter(): CustomLoginFilter {
        val customLoginFilter = CustomLoginFilter(objectMapper, authenticationManager())

        customLoginFilter.setAuthenticationManager(authenticationManager())
        customLoginFilter.setAuthenticationSuccessHandler(loginSuccessHandler)
        customLoginFilter.setAuthenticationFailureHandler(loginFailureHandler)

        return customLoginFilter
    }

    @Bean
    fun jwtAuthenticationFilter(): JwtAuthenticationFilter {
        return JwtAuthenticationFilter(
            accessTokenHeader = this.accessTokenHeader,
            refreshTokenHeader = this.refreshTokenHeader,
            tokenProvider = this.tokenProvider,
            userRepository = this.userRepository,
        )
    }
}
