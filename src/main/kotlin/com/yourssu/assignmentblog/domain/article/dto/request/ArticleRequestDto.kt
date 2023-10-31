package com.yourssu.assignmentblog.domain.article.dto.request

import com.yourssu.assignmentblog.global.common.dto.BaseRequestDto
import javax.validation.constraints.NotBlank

data class ArticleRequestDto(
    @field: NotBlank(message = "title은 누락되면 안 됩니다.")
    val title: String = "",

    @field: NotBlank(message = "content는 누락되면 안 됩니다.")
    val content: String = ""
): BaseRequestDto()