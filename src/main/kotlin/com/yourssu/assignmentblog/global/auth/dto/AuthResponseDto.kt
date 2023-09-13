package com.yourssu.assignmentblog.global.auth.dto

data class AuthResponseDto(
    val success: Boolean,
    val message: String,
    val accessToken: String,
    val refreshToken: String
)