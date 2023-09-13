package com.yourssu.assignmentblog.global.auth.dto.request

data class AuthResponseDto(
    val success: Boolean = false,
    val message: String = "",
    val accessToken: String = "",
    val refreshToken: String = ""
)