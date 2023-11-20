package com.yourssu.assignmentblog.global.util.resolver

import com.yourssu.assignmentblog.global.auth.jwt.AuthInfo
import com.yourssu.assignmentblog.global.auth.jwt.token.TokenExtractor
import com.yourssu.assignmentblog.global.util.annotation.Auth
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class UserAuthArgumentResolver(
    @Value("\${jwt.access.header}")
    private val accessTokenHeader: String,
) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.parameterType == AuthInfo::class.java &&
            parameter.hasParameterAnnotation(Auth::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): Any {
        val request = webRequest.nativeRequest as HttpServletRequest
        val response = webRequest.nativeResponse as HttpServletResponse

        val token: String = TokenExtractor.extractToken(accessTokenHeader, request, response)!!

        val userEmail: String = TokenExtractor.extractEmailFromToken(token, response)

        return AuthInfo(userEmail)
    }
}
