package com.yourssu.assignmentblog.global.auth.filter

import com.yourssu.assignmentblog.global.common.uri.RequestURI
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthenticationFilter: OncePerRequestFilter() {

    companion object {
        private val NO_CHECK_URIS: List<String> = arrayListOf(
            RequestURI.SIGNUP,
            RequestURI.LOGIN
        )
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

    }
}