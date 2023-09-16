package com.yourssu.assignmentblog.domain.article.dto.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class ArticleWriteRequestDto(
    @field: Email
    val email: String = "",

    @field: NotBlank
    val password: String = "",

    @field: NotBlank
    val title: String = "",

    @field: NotBlank
    val content: String = ""
)