package com.yourssu.assignmentblog.global.auth.jwt.token

import com.yourssu.assignmentblog.global.auth.jwt.JwtTokenManager
import com.yourssu.assignmentblog.global.util.sender.ResponseSender
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class TokenExtractor(
    @Value("\${jwt.secretKey}")
    private val _secretKey: String
) {

    init {
        secretKey = _secretKey
    }

    companion object {
        private lateinit var secretKey: String

        fun extractToken(
            headerName: String,
            request: HttpServletRequest,
            response: HttpServletResponse
        ): String? {
            val token: String = request.getHeader(headerName)

            return TokenChecker.removeBearer(token, response)
        }

        fun extractEmailFromToken(accessToken: String, response: HttpServletResponse): String {
            val key = Keys.hmacShaKeyFor(secretKey.toByteArray(StandardCharsets.UTF_8))

            return try {
                Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .body[JwtTokenManager.EMAIL]
                    .toString()
            } catch (e: Exception) {
                ResponseSender.setBadRequestResponse(response, "인증 실패: access token에 email claim이 없습니다.")
                throw IllegalArgumentException(e.message)
            }
        }
    }
}