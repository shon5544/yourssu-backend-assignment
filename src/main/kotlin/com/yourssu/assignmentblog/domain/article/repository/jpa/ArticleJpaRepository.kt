package com.yourssu.assignmentblog.domain.article.repository.jpa

import com.yourssu.assignmentblog.domain.article.domain.Article
import com.yourssu.assignmentblog.domain.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface ArticleJpaRepository: JpaRepository<Article, Long> {
    fun findByUser(user: User): Article?
}