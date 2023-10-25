package com.yourssu.assignmentblog.global.auth.jwt.token

import com.yourssu.assignmentblog.domain.user.repository.UserRepository
import com.yourssu.assignmentblog.global.util.sender.ResponseSender
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import javax.servlet.http.HttpServletResponse

@Component
class TokenChecker(
    @Value("\${jwt.secretKey}")
    private val _secretKey: String,

    _userRepository: UserRepository
) {
    init {
        secretKey = _secretKey
        userRepository = _userRepository
    }

    companion object {
        private lateinit var secretKey: String
        private lateinit var userRepository: UserRepository

        private const val BEARER = "Bearer "

        fun removeBearer(token: String?, response: HttpServletResponse): String? {
            if (token != null) {
                if (token.startsWith(BEARER)) {
                    token.replace(BEARER, "")

                    return if (isTokenValid(token)) {
                        token
                    } else {
                        ResponseSender.setBadRequestResponse(response, "인증 실패: 유효한 refresh 토큰이 아닙니다. " +
                                "로그인을 통해 토큰을 재발급 받으세요.")

                        throw IllegalArgumentException("요청 헤더에서 토큰 추출 실패: 유효한 토큰이 아닙니다.")
                    }
                }

                throw IllegalArgumentException("요청 헤더에서 토큰 추출 실패: 올바른 유형의 토큰이 아닙니다.")
            }

            return null
        }

        fun isRefreshTokenInDB(refreshToken: String): Boolean {
            val user = userRepository.findByRefreshToken(refreshToken)

            return user != null
        }

        private fun isTokenValid(token: String): Boolean {
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
}