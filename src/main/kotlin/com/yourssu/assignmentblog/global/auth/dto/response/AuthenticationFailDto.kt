package com.yourssu.assignmentblog.global.auth.dto.response

data class AuthenticationFailDto(
    val success: Boolean = false,
    val message: String = "",
)
