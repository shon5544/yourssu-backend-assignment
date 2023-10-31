package com.yourssu.assignmentblog.domain.article.service

import com.yourssu.assignmentblog.domain.article.domain.Article
import com.yourssu.assignmentblog.domain.article.dto.request.ArticleRequestDto
import com.yourssu.assignmentblog.domain.article.dto.response.ArticleResponseDto
import com.yourssu.assignmentblog.domain.article.repository.ArticleRepository
import com.yourssu.assignmentblog.domain.user.domain.User
import com.yourssu.assignmentblog.domain.user.repository.UserRepository
import com.yourssu.assignmentblog.global.common.aop.ExistenceCheckAdviceHolder
import com.yourssu.assignmentblog.global.common.aop.OwnershipCheckAdviceHolder
import com.yourssu.assignmentblog.global.common.enums.FailedMethod
import com.yourssu.assignmentblog.global.common.enums.FailedTargetType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ArticleService(
    private val articleRepository: ArticleRepository,
    private val userRepository: UserRepository,
) {

    @Transactional
    fun write(
        requestDto: ArticleRequestDto,
        email: String
    ): ArticleResponseDto = ExistenceCheckAdviceHolder.checkUserAccount(
        currentURI = requestDto.currentURI,
        email = email,
        failedTargetText = requestDto.failedTargetText
    ) {
        val user = userRepository.findByEmail(email)

        val article = Article(
            content = requestDto.content,
            title = requestDto.title,
            user = user
        )

        ArticleResponseDto(
            articleRepository.save(article),
            email
        )
    }

    @Transactional
    fun edit(
        articleId: Long,
        requestDto: ArticleRequestDto,
        email: String,
    ): ArticleResponseDto = ExistenceCheckAdviceHolder.checkUserAccount(
        currentURI = requestDto.currentURI,
        email = email,
        failedTargetText = requestDto.failedTargetText
    ) {

        val currentURI = requestDto.currentURI
        val failedTargetText = requestDto.failedTargetText

        return@checkUserAccount ExistenceCheckAdviceHolder.checkArticleExistence(
            articleId = articleId,
            currentURI = currentURI,
            failedTargetText = failedTargetText
        ) {

            val article: Article = articleRepository.findById(articleId)!!
            val user: User = userRepository.findByEmail(email)!!

            article.update(requestDto, user)

            return@checkArticleExistence ArticleResponseDto(
                article, article.user!!.email
            )
        }
    }

    @Transactional
    fun delete(
        articleId: Long,
        currentURI: String,
        email: String,
        failedTargetText: String = "${FailedTargetType.ARTICLE} ${FailedMethod.DELETE}"
    ) = ExistenceCheckAdviceHolder.checkUserAccount(
        currentURI = currentURI,
        failedTargetText = failedTargetText,
        email = email
    ) {

        ExistenceCheckAdviceHolder.checkArticleExistence(
            articleId = articleId,
            failedTargetText = failedTargetText,
            currentURI = currentURI
        ) {
            val article: Article = articleRepository.findById(articleId)!!
            val user: User = userRepository.findByEmail(email)!!

            OwnershipCheckAdviceHolder.checkOwnership(
                target = article,
                currentURI = currentURI,
                user = user,
                failedTargetText = failedTargetText
            ) {
                articleRepository.delete(article)
            }
        }
    }
}