package com.yourssu.assignmentblog.global.auth.dto

data class AuthenticationFailDto(
    val isSuccess: Boolean,
    val message: String
) {
}