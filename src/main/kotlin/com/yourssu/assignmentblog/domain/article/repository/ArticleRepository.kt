package com.yourssu.assignmentblog.domain.article.repository

import com.yourssu.assignmentblog.domain.article.domain.Article
import com.yourssu.assignmentblog.domain.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository


interface ArticleJpaRepository: JpaRepository<Article, Long> {
    fun findByUser(user: User): Article?
}

interface ArticleRepository {
    fun save(article: Article): Article
    fun findById(id: Long): Article?
    fun delete(article: Article)
}

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

    override fun delete(article: Article) {
        return articleJpaRepository.delete(article)
    }
}