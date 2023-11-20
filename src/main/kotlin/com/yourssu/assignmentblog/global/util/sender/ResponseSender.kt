package com.yourssu.assignmentblog.global.util.sender

import com.fasterxml.jackson.databind.ObjectMapper
import com.yourssu.assignmentblog.global.auth.dto.response.AuthenticationFailDto
import com.yourssu.assignmentblog.global.auth.jwt.ReIssuedTokens
import org.springframework.stereotype.Component
import java.io.IOException
import javax.servlet.http.HttpServletResponse

@Component
class ResponseSender(
    _objectMapper: ObjectMapper,
) {
    init {
        objectMapper = _objectMapper
    }

    companion object {
        private lateinit var objectMapper: ObjectMapper

        @Throws(IOException::class)
        fun setBadRequestResponse(
            response: HttpServletResponse,
            message: String,
        ) {
            val responseDto = AuthenticationFailDto(false, message)
            val result: String = objectMapper.writeValueAsString(responseDto)

            response.status = HttpServletResponse.SC_BAD_REQUEST
            response.characterEncoding = "UTF-8"
            response.contentType = "application/json"
            response.writer.write(result)
        }

        fun sendAccessTokenAndRefreshToken(
            response: HttpServletResponse,
            reIssueResponseDto: ReIssuedTokens,
        ) {
            val result = objectMapper.writeValueAsString(reIssueResponseDto)

            response.status = HttpServletResponse.SC_OK
            response.characterEncoding = "UTF-8"
            response.contentType = "application/json"
            response.writer.write(result)
        }
    }
}
