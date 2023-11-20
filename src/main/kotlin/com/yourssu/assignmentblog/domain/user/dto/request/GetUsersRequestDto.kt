package com.yourssu.assignmentblog.domain.user.dto.request

import java.time.LocalDate

data class GetUsersRequestDto(
    val username: String?,
    val email: String?,
    val createdAtStart: LocalDate?,
    val createdAtEnd: LocalDate?,
)
