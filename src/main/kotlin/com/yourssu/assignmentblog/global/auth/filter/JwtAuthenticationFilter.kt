package com.yourssu.assignmentblog.global.auth.filter

import com.yourssu.assignmentblog.domain.user.domain.User
import com.yourssu.assignmentblog.domain.user.repository.UserRepository
import com.yourssu.assignmentblog.global.auth.jwt.ReIssuedTokens
import com.yourssu.assignmentblog.global.auth.jwt.token.TokenChecker
import com.yourssu.assignmentblog.global.auth.jwt.token.TokenExtractor
import com.yourssu.assignmentblog.global.auth.jwt.token.TokenProvider
import com.yourssu.assignmentblog.global.auth.service.CustomUserDetails
import com.yourssu.assignmentblog.global.common.uri.RequestURI
import com.yourssu.assignmentblog.global.util.sender.ResponseSender
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthenticationFilter(
    private val accessTokenHeader: String,

    private val refreshTokenHeader: String,

    private val tokenProvider: TokenProvider,

    private val userRepository: UserRepository,

    private val authoritiesMapper: GrantedAuthoritiesMapper = NullAuthoritiesMapper()
): OncePerRequestFilter() {

    companion object {
        private val NO_CHECK_URIS: List<String> = arrayListOf(
            RequestURI.USER + "/signup",
            RequestURI.USER + "/login",
            "/swagger-ui/index.html",
            "/favicon.ico"
        )
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (NO_CHECK_URIS.contains(request.requestURI)
            || request.requestURI.contains("/swagger-ui")
            || request.requestURI.contains("/v3")
        ) {
            filterChain.doFilter(request, response)
            return
        }

        val accessToken = TokenExtractor.extractToken(accessTokenHeader, request, response)
        val refreshToken = TokenExtractor.extractToken(refreshTokenHeader, request, response)

        if (refreshToken != null) {

            if(!TokenChecker.isRefreshTokenInDB(refreshToken)) {
                ResponseSender.setBadRequestResponse(response, "인증 실패: 해당 refresh 토큰에 대한 기록이 없습니다. " +
                        "로그인을 통해 토큰을 재발급 받으세요.")
                return
            }

            val reIssuedTokens: ReIssuedTokens = tokenProvider.reIssueTokens(refreshToken)
            ResponseSender.sendAccessTokenAndRefreshToken(response, reIssuedTokens)
            return
        }

        if (accessToken != null) {

            val userEmail = TokenExtractor.extractEmailFromToken(accessToken, response)
            val user = userRepository.findByEmail(userEmail)

            if (user == null) {
                ResponseSender.setBadRequestResponse(response, "인증 실패: access token에 기재된 email을 가진 User가 없습니다.")
                return
            }

            setAuthentication(user)
            filterChain.doFilter(request, response)
            return
        }

        ResponseSender.setBadRequestResponse(response, "인증 실패: access token, refresh token 모두 누락되었습니다.")
        return
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