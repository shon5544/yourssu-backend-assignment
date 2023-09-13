package com.yourssu.assignmentblog.security.jwt

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class JwtTokenManager(
    @Value("\${jwt.secretKey}")
    private val secretKey: String,

    @Value("\${jwt.access.expiration}")
    private val accessTokenExpiration: Int,

    @Value("\${jwt.refresh.expiration}")
    private val refreshTokenExpiration: Int,

    @Value("\${jwt.access.header}")
    private val accessTokenHeader: String,

    @Value("\${jwt.refresh.header}")
    private val refreshTokenHeader: String
) {
    companion object {
        const val ACCESS_TOKEN_SUBJECT = "AccessToken"
        const val REFRESH_TOKEN_SUBJECT = "RefreshToken"
        const val EMAIL = "email"
        const val BEARER = "bearer "
        const val AUTHORITES_KEY = "auth"
    }

    /*
    만들어야 할 메소드
    - 액세스 토큰 생성
    - 리프레시 토큰 생성
    - 액세스 토큰 추출
    - 리프레시 토큰 추출
    - 액세스 토큰 검증
    - 리프레시 토큰 검증
    */
}