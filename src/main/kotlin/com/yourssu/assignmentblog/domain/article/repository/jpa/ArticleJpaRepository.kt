package com.yourssu.assignmentblog.domain.article.repository.jpa

import com.yourssu.assignmentblog.domain.article.domain.Article
import org.springframework.data.jpa.repository.JpaRepository

interface ArticleJpaRepository: JpaRepository<Article, Long> {

}