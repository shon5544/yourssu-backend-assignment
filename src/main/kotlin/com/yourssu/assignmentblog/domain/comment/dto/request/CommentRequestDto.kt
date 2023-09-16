package com.yourssu.assignmentblog.domain.comment.dto.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class CommentRequestDto(
    @field: Email(message = "Email은 누락되면 안 됩니다.")
    val email: String = "",

    @field: NotBlank(message = "Password는 누락되면 안 됩니다.")
    val password: String = "",

    @field: NotBlank(message = "content는 누락되면 안 됩니다.")
    val content: String = ""
)