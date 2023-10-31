package com.yourssu.assignmentblog.global.auth.jwt.token

import com.yourssu.assignmentblog.domain.user.domain.User
import com.yourssu.assignmentblog.domain.user.repository.UserRepository
import com.yourssu.assignmentblog.global.auth.jwt.ReIssuedTokens
import com.yourssu.assignmentblog.global.common.localDateTImeHolder.ICustomLocalDateTime
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.*
import javax.persistence.EntityNotFoundException
import javax.transaction.Transactional

@Component
class TokenProvider(
    @Value("\${jwt.secretKey}")
    private val secretKey: String,

    @Value("\${jwt.access.expiration}")
    private val accessTokenExpiration: Long,

    @Value("\${jwt.refresh.expiration}")
    private val refreshTokenExpiration: Long,

    private val localDateTime: ICustomLocalDateTime,

    private val userRepository: UserRepository
) {
    companion object {
        const val ACCESS_TOKEN_SUBJECT = "AccessToken"
        const val REFRESH_TOKEN_SUBJECT = "RefreshToken"
        const val EMAIL = "email"
    }

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

    @Transactional
    fun reIssueTokens(refreshToken: String): ReIssuedTokens {
        val user = userRepository.findByRefreshToken(refreshToken)

        val newAccessToken: String =
            if (user != null) createAccessToken(user)
            else throw EntityNotFoundException("토큰 재발급 실패: 해당 refresh token을 가진 User가 없습니다. 일반적으로 일어나기 힘든 상황입니다.")
        val newRefreshToken: String = createRefreshToken()

        user.refreshToken = refreshToken

        return ReIssuedTokens(newAccessToken, newRefreshToken)
    }
}