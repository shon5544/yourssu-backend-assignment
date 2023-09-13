package com.yourssu.assignmentblog.security.jwt

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
        const val ACCESS_TOKEN_SUBJECT = "AccessToken"
        const val REFRESH_TOKEN_SUBJECT = "RefreshToken"
        const val EMAIL = "email"
        const val BEARER = "bearer ";
    }

}