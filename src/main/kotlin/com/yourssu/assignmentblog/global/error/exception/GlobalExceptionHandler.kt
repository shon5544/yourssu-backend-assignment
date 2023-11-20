package com.yourssu.assignmentblog.global.error.exception

import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(CustomException::class)
    fun handler(e: CustomException): ExceptionResponseDto {
        return ExceptionResponseDto(
            status = e.status.name,
            message = e.message,
            requestURI = e.requestURI,
        )
    }
}
