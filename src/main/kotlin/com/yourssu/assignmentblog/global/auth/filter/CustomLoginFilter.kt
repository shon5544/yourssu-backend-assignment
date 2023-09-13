package com.yourssu.assignmentblog.global.auth.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.yourssu.assignmentblog.global.auth.dto.response.LoginRequestDto
import com.yourssu.assignmentblog.global.common.uri.RequestURI
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.util.StreamUtils
import java.nio.charset.StandardCharsets
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CustomLoginFilter(
    private val objectMapper: ObjectMapper,
    private val authenticationManager: AuthenticationManager,
    private val contentTypes: List<String> = arrayListOf(
        "application/json",
        "application/json; charset=UTF-8",
        "application/json;charset=UTF-8")
): AbstractAuthenticationProcessingFilter(RequestURI.USER + "/login") {

    override fun attemptAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse
    ): Authentication {
        val body: String = StreamUtils.copyToString(request.inputStream, StandardCharsets.UTF_8)
        val contentType: String? = request.contentType

        if (contentType == null || !contentTypes.contains(contentType))
            throw AuthenticationServiceException(
                "로그인 실패: 지원하지 않는 요청 content type입니다.($contentType)")

        val mappedBody: LoginRequestDto = objectMapper.readValue(body, LoginRequestDto::class.java)

        val usernamePasswordAuthenticationToken =
            UsernamePasswordAuthenticationToken(mappedBody.email, mappedBody.password)

        return authenticationManager.authenticate(usernamePasswordAuthenticationToken)
    }
}