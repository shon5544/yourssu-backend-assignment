package com.yourssu.assignmentblog.global.auth.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import com.yourssu.assignmentblog.global.common.localDateTImeHolder.ICustomLocalDateTime
import com.yourssu.assignmentblog.domain.user.domain.User
import com.yourssu.assignmentblog.domain.user.repository.UserRepository
import com.yourssu.assignmentblog.global.auth.dto.response.AuthenticationFailDto
import com.yourssu.assignmentblog.global.util.sender.ResponseSender
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.*
import javax.persistence.EntityNotFoundException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.transaction.Transactional
import kotlin.IllegalArgumentException

@Component
class JwtTokenManager(
    @Value("\${jwt.secretKey}")
    private val secretKey: String,

    @Value("\${jwt.access.expiration}")
    private val accessTokenExpiration: Long,

    @Value("\${jwt.refresh.expiration}")
    private val refreshTokenExpiration: Long,

    private val localDateTime: ICustomLocalDateTime,

    private val userRepository: UserRepository,

    private val objectMapper: ObjectMapper = ObjectMapper()
) {
    companion object {
        const val ACCESS_TOKEN_SUBJECT = "AccessToken"
        const val REFRESH_TOKEN_SUBJECT = "RefreshToken"
        const val EMAIL = "email"
        const val BEARER = "Bearer "
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


    fun extractToken(
        headerName: String,
        request: HttpServletRequest,
        response: HttpServletResponse
    ): String {
        val token: String = request.getHeader(headerName)

        if (token.startsWith(BEARER)) {
            val result = token.replace(BEARER, "")
            println(result)

            return if (isTokenValid(result)) result else throw IllegalArgumentException("요청 헤더에서 토큰 추출 실패: 유효한 토큰이 아닙니다.")
        }

        setBadRequestResponse(response, "인증 실패: 유효한 refresh 토큰이 아닙니다. " +
                "로그인을 통해 토큰을 재발급 받으세요.")

        throw IllegalArgumentException("요청 헤더에서 토큰 추출 실패: 토큰의 형식이 잘못됐습니다.")
    }

    @Throws(IOException::class)
    fun setBadRequestResponse(response: HttpServletResponse, message: String) {
        return
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

    fun extractEmailFromToken(accessToken: String): String {
        val key = Keys.hmacShaKeyFor(secretKey.toByteArray(StandardCharsets.UTF_8))

        return try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .body[EMAIL]
                .toString()
        } catch (e: Exception) {
            throw IllegalArgumentException(e.message)
        }
    }
}