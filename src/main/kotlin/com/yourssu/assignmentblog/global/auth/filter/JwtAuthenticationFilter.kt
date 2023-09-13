package com.yourssu.assignmentblog.global.auth.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.yourssu.assignmentblog.domain.user.domain.User
import com.yourssu.assignmentblog.domain.user.repository.UserRepository
import com.yourssu.assignmentblog.global.auth.dto.AuthResponseDto
import com.yourssu.assignmentblog.global.auth.dto.AuthenticationFailDto
import com.yourssu.assignmentblog.global.auth.jwt.JwtTokenManager
import com.yourssu.assignmentblog.global.auth.jwt.ReIssuedTokens
import com.yourssu.assignmentblog.global.auth.service.CustomUserDetails
import com.yourssu.assignmentblog.global.common.uri.RequestURI
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthenticationFilter(
    private val accessTokenHeader: String,

    private val refreshTokenHeader: String,

    private val jwtTokenManager: JwtTokenManager,

    private val objectMapper: ObjectMapper,

    private val userRepository: UserRepository,

    private val authoritiesMapper: GrantedAuthoritiesMapper = NullAuthoritiesMapper()
): OncePerRequestFilter() {

    companion object {
        private val NO_CHECK_URIS: List<String> = arrayListOf(
            RequestURI.USER + "/signup",
            RequestURI.USER + "/login"
        )
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (NO_CHECK_URIS.contains(request.requestURI)) {
            filterChain.doFilter(request, response)
            return
        }

        val accessToken = jwtTokenManager.extractToken(accessTokenHeader, request)
        val refreshToken = jwtTokenManager.extractToken(refreshTokenHeader, request)

        if (refreshToken != null) {
            if (!jwtTokenManager.isTokenValid(refreshToken)) {
                setBadRequestResponse(response, "인증 실패: 유효한 refresh 토큰이 아닙니다. " +
                        "로그인을 통해 토큰을 재발급 받으세요.")

                return
            }

            if(!isRefreshTokenInDB(refreshToken)) {
                setBadRequestResponse(response, "인증 실패: 해당 refresh 토큰에 대한 기록이 없습니다. " +
                        "로그인을 통해 토큰을 재발급 받으세요.")

                return
            }

            val reIssuedTokens: ReIssuedTokens = jwtTokenManager.reIssueTokens(refreshToken)

            sendAccessTokenAndRefreshToken(
                response,
                reIssuedTokens.accessToken,
                reIssuedTokens.refreshToken)
            return
        }

        if (accessToken != null) {
            if (!jwtTokenManager.isTokenValid(accessToken)) {
                setBadRequestResponse(response, "인증 실패: 유효한 access token이 아닙니다. " +
                        "로그인을 통해 토큰을 재발급 받으세요.")

                return
            }

            val userEmail = jwtTokenManager.extractEmailFromToken(accessToken)
            if (userEmail == null) {
                setBadRequestResponse(response, "인증 실패: access token에 email claim이 없습니다.")
                return
            }

            val user = userRepository.findByEmail(userEmail)

            if (user == null) {
                setBadRequestResponse(response, "인증 실패: access token에 기재된 email을 가진 User가 없습니다.")

                return
            }

            setAuthentication(user)

            filterChain.doFilter(request, response)
            return
        }

        setBadRequestResponse(response, "인증 실패: access token, refresh token 모두 누락되었습니다.")
        return
    }

    @Throws(IOException::class)
    private fun setBadRequestResponse(response: HttpServletResponse, message: String) {
        val responseDto = AuthenticationFailDto(false, message)
        val result: String = objectMapper.writeValueAsString(responseDto)

        response.status = HttpServletResponse.SC_BAD_REQUEST
        response.characterEncoding = "UTF-8"
        response.contentType = "application/json"
        response.writer.write(result)
    }

    private fun sendAccessTokenAndRefreshToken(
        response: HttpServletResponse,
        accessToken: String,
        refreshToken: String) {

        val responseDto = AuthResponseDto(true, "토큰 재발급 성공.", accessToken, refreshToken)
        val result = objectMapper.writeValueAsString(responseDto)

        response.status = HttpServletResponse.SC_BAD_REQUEST
        response.characterEncoding = "UTF-8"
        response.contentType = "application/json"
        response.writer.write(result)
    }

    private fun isRefreshTokenInDB(refreshToken: String): Boolean {
        val user = userRepository.findByRefreshToken(refreshToken)

        return user != null
    }

    private fun setAuthentication(user: User) {
        val userDetails: UserDetails = CustomUserDetails(user)

        val authentication: Authentication = UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            authoritiesMapper.mapAuthorities(userDetails.authorities)
        )

        SecurityContextHolder.getContext().authentication = authentication
    }
}