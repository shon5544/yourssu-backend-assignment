package com.yourssu.assignmentblog.global.common.exception

import org.springframework.http.HttpStatus

class CustomException(
    val status: HttpStatus,
    override val message: String,
    val requestURI: String
): RuntimeException() {
}