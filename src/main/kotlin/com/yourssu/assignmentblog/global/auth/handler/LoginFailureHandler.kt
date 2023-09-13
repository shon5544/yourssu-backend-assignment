package com.yourssu.assignmentblog.global.auth.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.yourssu.assignmentblog.global.auth.dto.AuthenticationFailDto
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class LoginFailureHandler(private val objectMapper: ObjectMapper): SimpleUrlAuthenticationFailureHandler() {

    override fun onAuthenticationFailure(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AuthenticationException
    ) {
        val responseDto = AuthenticationFailDto(false, "로그인 실패: ${exception.message}")

        val result = objectMapper.writeValueAsString(responseDto)

        response.status = HttpServletResponse.SC_BAD_REQUEST
        response.characterEncoding = "UTF-8"
        response.contentType = "application/json"
        response.writer.write(result)
    }
}