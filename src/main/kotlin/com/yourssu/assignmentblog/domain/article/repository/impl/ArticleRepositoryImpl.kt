package com.yourssu.assignmentblog.domain.article.repository.impl

import com.yourssu.assignmentblog.domain.article.domain.Article
import com.yourssu.assignmentblog.domain.article.repository.ArticleRepository
import com.yourssu.assignmentblog.domain.article.repository.jpa.ArticleJpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class ArticleRepositoryImpl(
    private val articleJpaRepository: ArticleJpaRepository
): ArticleRepository {
    override fun save(article: Article): Article {
        return articleJpaRepository.save(article)
    }

    override fun findById(id: Long): Article? {
        return articleJpaRepository.findByIdOrNull(id)
    }
}