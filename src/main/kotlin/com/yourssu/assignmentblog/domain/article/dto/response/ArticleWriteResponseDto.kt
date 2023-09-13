package com.yourssu.assignmentblog.domain.article.dto.response

import com.yourssu.assignmentblog.domain.article.domain.Article

data class ArticleWriteResponseDto(
    var articleId: Long? = null,
    var email: String = "",
    var title: String = "",
    var content: String = ""
) {
    constructor(article: Article, email: String): this() {
        this.articleId = article.id
        this.email = email
        this.title = article.title
        this.content = article.content
    }
}