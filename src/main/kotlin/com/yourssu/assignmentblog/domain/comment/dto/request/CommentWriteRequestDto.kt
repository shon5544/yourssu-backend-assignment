package com.yourssu.assignmentblog.domain.comment.dto.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class CommentWriteRequestDto(
    @field: Email
    val email: String = "",

    @field: NotBlank
    val password: String = "",

    @field: NotBlank
    val content: String = ""
)