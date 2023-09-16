package com.yourssu.assignmentblog.global.auth.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.yourssu.assignmentblog.domain.user.repository.UserRepository
import com.yourssu.assignmentblog.global.auth.dto.response.AuthResponseDto
import com.yourssu.assignmentblog.global.auth.jwt.JwtTokenManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import javax.persistence.EntityNotFoundException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.transaction.Transactional

@Component
class LoginSuccessHandler(
    private val jwtTokenManager: JwtTokenManager,
    private val userRepository: UserRepository,
    private val objectMapper: ObjectMapper
): SimpleUrlAuthenticationSuccessHandler() {

    private fun extractEmailInUserDetails(authentication: Authentication): String {
        val userDetails: UserDetails = authentication.principal as UserDetails

        return userDetails.username
    }

    @Transactional
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val email = extractEmailInUserDetails(authentication)

        val user = userRepository.findByEmail(email) ?: throw EntityNotFoundException("로그인 실패: 해당 email을 가진 User가 없습니다.")

        val accessToken = jwtTokenManager.createAccessToken(user)
        val refreshToken = jwtTokenManager.createRefreshToken()

        user.refreshToken = refreshToken

        val responseDto = AuthResponseDto(true, "로그인 성공.", accessToken, refreshToken)

        val result = objectMapper.writeValueAsString(responseDto)

        response.status = HttpServletResponse.SC_OK
        response.characterEncoding = "UTF-8"
        response.contentType = "application/json"
        response.writer.write(result)
    }
}