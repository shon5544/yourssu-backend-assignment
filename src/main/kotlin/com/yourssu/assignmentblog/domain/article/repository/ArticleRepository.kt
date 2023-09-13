package com.yourssu.assignmentblog.domain.article.repository

import com.yourssu.assignmentblog.domain.article.domain.Article

interface ArticleRepository {
    fun save(article: Article): Article
    fun findById(id: Long): Article?
}