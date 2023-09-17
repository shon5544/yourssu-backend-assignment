package com.yourssu.assignmentblog.domain.user.repository.stub

import com.yourssu.assignmentblog.domain.article.domain.Article
import com.yourssu.assignmentblog.domain.article.repository.ArticleRepository

class TestArticleRepository: ArticleRepository {

    private var article: Article? = null

    override fun save(article: Article): Article {
        this.article = article
        return article
    }

    override fun findById(id: Long): Article? {
        return article
    }

    override fun delete(article: Article) {
        return
    }
}