package com.yourssu.assignmentblog.global.auth.dto.response

data class AuthResponseDto(
    val success: Boolean = false,
    val message: String = "",
    val accessToken: String = "",
    val refreshToken: String = ""
)