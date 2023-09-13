package com.yourssu.assignmentblog.security.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class JwtTokenManager(
    @Value("\${jwt.secretKey}")
    private val secretKey: String,

    @Value("\${jwt.access.expiration}")
    private val accessTokenExpiration: Long,

    @Value("\${jwt.refresh.expiration}")
    private val refreshTokenExpiration: Long,

    @Value("\${jwt.access.header}")
    private val accessTokenHeader: String,

    @Value("\${jwt.refresh.header}")
    private val refreshTokenHeader: String,
) {
    companion object {
        val ACCESS_TOKEN_SUBJECT = "AccessToken"
        val REFRESH_TOKEN_SUBJECT = "RefreshToken"
        val EMAIL = "email"
        val BEARER = "bearer ";
    }

}