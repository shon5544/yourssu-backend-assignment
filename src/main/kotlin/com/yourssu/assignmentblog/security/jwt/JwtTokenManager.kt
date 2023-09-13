package com.yourssu.assignmentblog.security.jwt

import com.yourssu.assignmentblog.localDateTImeHolder.LocalDateTimeHolder
import com.yourssu.assignmentblog.user.domain.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.servlet.http.HttpServletRequest

@Component
class JwtTokenManager(
    @Value("\${jwt.secretKey}")
    private val secretKey: String,

    @Value("\${jwt.access.expiration}")
    private val accessTokenExpiration: Long,

    @Value("\${jwt.refresh.expiration}")
    private val refreshTokenExpiration: Long,

    private val localDateTime: LocalDateTimeHolder
) {
    companion object {
        const val ACCESS_TOKEN_SUBJECT = "AccessToken"
        const val REFRESH_TOKEN_SUBJECT = "RefreshToken"
        const val EMAIL = "email"
        const val BEARER = "bearer "
    }

    /*
    만들어야 할 메소드
    - 액세스 토큰 생성
    - 리프레시 토큰 생성
    - 토큰 추출
    - 토큰 검증
    */
    fun createAccessToken(user: User): String {
        val key = Keys.hmacShaKeyFor(secretKey.toByteArray(StandardCharsets.UTF_8))

        return Jwts.builder()
            .setSubject(ACCESS_TOKEN_SUBJECT)
            .claim(EMAIL, user.email)
            .setExpiration(
                Date.from(
                    localDateTime
                        .plusHour(accessTokenExpiration)
                )
            )
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()!!
    }

    fun createRefreshToken(): String {
        val key = Keys.hmacShaKeyFor(secretKey.toByteArray(StandardCharsets.UTF_8))

        return Jwts.builder()
            .setSubject(REFRESH_TOKEN_SUBJECT)
            .setExpiration(
                Date.from(
                    localDateTime
                        .plusHour(refreshTokenExpiration)
                )
            )
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }


    fun extractToken(headerName: String, request: HttpServletRequest): String {
        val token: String = request.getHeader(headerName)

        return if (token.startsWith(BEARER)) {
            token.replace(BEARER, "")
        } else {
            throw IllegalArgumentException("액세스 토큰 헤더 추출 실패: 토큰의 형식이 잘못됐습니다.")
        }
    }

    fun isTokenValid(token: String): Boolean {
        val key = Keys.hmacShaKeyFor(secretKey.toByteArray(StandardCharsets.UTF_8))

        return try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)

            true
        } catch (e: Exception) {
            false
        }
    }
}