package com.yourssu.assignmentblog.global.auth.dto.response

data class TokenReIssueResponseDto(
    val success: Boolean = false,
    val message: String = "",
    val accessToken: String = "",
    val refreshToken: String = ""
)