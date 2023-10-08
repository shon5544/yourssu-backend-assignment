package com.yourssu.assignmentblog.global.auth.dto.response

data class AuthResponseDto(
    val email: String = "",
    val username: String = "",
    val role: String = "",
    val accessToken: String = "",
    val refreshToken: String = ""
)