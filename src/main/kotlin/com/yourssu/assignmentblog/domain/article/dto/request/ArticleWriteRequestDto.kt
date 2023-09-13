package com.yourssu.assignmentblog.domain.article.dto.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotNull

data class ArticleWriteRequestDto(
    @field:Email
    val email: String = "",

    @field:NotNull
    val password: String = "",

    @field:NotNull
    val title: String = "",

    @field:NotNull
    val content: String = ""
)