package com.yourssu.assignmentblog.domain.article.dto.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class ArticleDeleteRequestDto(
    @field: Email
    val email: String = "",

    @field: NotBlank
    val password: String = ""
)