package com.yourssu.assignmentblog.domain.user.dto.response

import java.time.LocalDateTime

data class GetUsersResponseDto(
    val users: List<UserVO>
)

class UserVO(
    val id: Long,
    val email: String,
    val username: String,
    val role: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)