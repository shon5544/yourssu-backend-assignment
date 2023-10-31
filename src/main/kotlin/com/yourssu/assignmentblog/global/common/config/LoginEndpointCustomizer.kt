package com.yourssu.assignmentblog.global.common.config

import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.PathItem
import io.swagger.v3.oas.models.media.Content
import io.swagger.v3.oas.models.media.ObjectSchema
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.media.StringSchema
import io.swagger.v3.oas.models.parameters.RequestBody
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.responses.ApiResponses
import org.slf4j.LoggerFactory
import org.springdoc.core.customizers.OpenApiCustomiser
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import java.lang.Exception
import java.lang.reflect.Field
import kotlin.jvm.Throws

inline fun <reified T> T.logger() = LoggerFactory.getLogger(T::class.java)!!

class LoginEndpointCustomizer<JSON_FILTER: AbstractAuthenticationProcessingFilter> {

    fun loginEndpointCustomizer(filter: JSON_FILTER, tagName: String): OpenApiCustomiser {
        
        return OpenApiCustomiser { openAPI ->
            val operation = Operation()

            operation.requestBody(getLoginRequestBody())
            operation.responses(getLoginApiResponses())
            operation.addTagsItem(tagName)
            operation.summary("로그인")
            operation.description("사용자 로그인의 인증을 처리합니다.")

            val pathItem = PathItem().post(operation)

            try {
                openAPI.paths.addPathItem(getLoginPath(filter), pathItem)
            } catch (ignored: Exception) {
                logger().trace(ignored.message)
            }
        }
    }

    private fun getLoginRequestBody(): RequestBody {
        val schema: Schema<*> = ObjectSchema()
        schema.addProperty("email", StringSchema()._default("string"))
        schema.addProperty("password", StringSchema()._default("string"))

        return RequestBody().content(Content().addMediaType(
            MediaType.APPLICATION_JSON_VALUE,
            io.swagger.v3.oas.models.media.MediaType().schema(schema)
        ))
    }

    private fun getLoginApiResponses(): ApiResponses {
        val apiResponses = ApiResponses()
        apiResponses.addApiResponse(
            "${HttpStatus.OK.value()}",
            ApiResponse().description(HttpStatus.OK.reasonPhrase)
        )

        apiResponses.addApiResponse(
            "${HttpStatus.FORBIDDEN.value()}",
            ApiResponse().description(HttpStatus.FORBIDDEN.reasonPhrase)
        )

        return apiResponses
    }

    @Throws(Exception::class)
    private fun getLoginPath(filter: JSON_FILTER): String {
        val requestMatcherField: Field = AbstractAuthenticationProcessingFilter::class.java
            .getDeclaredField("requiresAuthenticationRequestMatcher")

        requestMatcherField.isAccessible = true

        val requestMatcher: AntPathRequestMatcher = requestMatcherField.get(filter) as AntPathRequestMatcher
        val loginPath = requestMatcher.pattern
        requestMatcherField.isAccessible = false
        return loginPath
    }
}