package com.yourssu.assignmentblog.global.common.exception

import java.time.LocalDateTime

data class ExceptionResponseDto(
    val time: LocalDateTime = LocalDateTime.now(),
    val status: String = "",
    val message: String = "",
    val requestURI: String = ""
)