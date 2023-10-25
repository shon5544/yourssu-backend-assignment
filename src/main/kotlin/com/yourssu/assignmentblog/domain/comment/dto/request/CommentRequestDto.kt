package com.yourssu.assignmentblog.domain.comment.dto.request

import javax.validation.constraints.NotBlank

data class CommentRequestDto(

    @field: NotBlank(message = "content는 누락되면 안 됩니다.")
    val content: String = ""
)