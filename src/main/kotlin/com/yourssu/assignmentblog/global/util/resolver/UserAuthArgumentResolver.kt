package com.yourssu.assignmentblog.global.util.resolver

import com.yourssu.assignmentblog.global.auth.jwt.AuthInfo
import com.yourssu.assignmentblog.global.auth.jwt.JwtTokenManager
import com.yourssu.assignmentblog.global.util.annotation.Auth
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import javax.servlet.http.HttpServletRequest

@Component
class UserAuthArgumentResolver(
    @Value("\${jwt.access.header}")
    private val accessTokenHeader: String,

    private val tokenManager: JwtTokenManager
): HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.parameterType == AuthInfo::class.java &&
                parameter.hasParameterAnnotation(Auth::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any {
        val httpServletRequest: HttpServletRequest = webRequest.nativeRequest as HttpServletRequest

        val token: String = tokenManager.extractToken(accessTokenHeader, httpServletRequest)

        val userEmail: String = tokenManager.extractEmailFromToken(token)

        return AuthInfo(userEmail)
    }
}