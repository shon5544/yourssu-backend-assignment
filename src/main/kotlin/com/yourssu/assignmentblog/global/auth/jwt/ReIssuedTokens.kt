package com.yourssu.assignmentblog.global.auth.jwt

data class ReIssuedTokens(
    val accessToken: String,
    val refreshToken: String
)