package com.yourssu.assignmentblog.domain.article.service

import com.yourssu.assignmentblog.domain.article.domain.Article
import com.yourssu.assignmentblog.global.common.dto.DeleteRequestDto
import com.yourssu.assignmentblog.domain.article.dto.request.ArticleRequestDto
import com.yourssu.assignmentblog.domain.article.dto.response.ArticleWriteResponseDto
import com.yourssu.assignmentblog.domain.article.repository.ArticleRepository
import com.yourssu.assignmentblog.global.common.domain.OwnershipChecker
import com.yourssu.assignmentblog.global.common.domain.ExistenceChecker
import com.yourssu.assignmentblog.global.common.enums.FailedMethod
import com.yourssu.assignmentblog.global.common.enums.FailedTargetType
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class ArticleService(
    private val articleRepository: ArticleRepository,
    private val existenceChecker: ExistenceChecker,
    private val ownershipChecker: OwnershipChecker
) {

    @Transactional
    fun write(requestDto: ArticleRequestDto, currentURI: String): ArticleWriteResponseDto {

        val failedTargetText = "${FailedTargetType.ARTICLE} ${FailedMethod.WRITE}"

        val user = existenceChecker.checkUserAccount(
            currentURI = currentURI,
            email = requestDto.email,
            password = requestDto.password,
            failedTargetText = failedTargetText,
        )

        val article = Article(
            content = requestDto.content,
            title = requestDto.title,
            user = user
        )

        return ArticleWriteResponseDto(
            articleRepository.save(article),
            user.email)
    }

    @Transactional
    fun edit(
        articleId: Long,
        currentURI: String,
        requestDto: ArticleRequestDto
        ): ArticleWriteResponseDto {

        val failedTargetText = "${FailedTargetType.ARTICLE} ${FailedMethod.EDIT}"

        val user = existenceChecker.checkUserAccount(
            currentURI = currentURI,
            email = requestDto.email,
            password = requestDto.password,
            failedTargetText = failedTargetText,
        )

        val article = existenceChecker.checkArticle(
            articleId = articleId,
            currentURI = currentURI,
            failedTarget = failedTargetText
        )

        ownershipChecker.check(
            target = article,
            currentURI = currentURI,
            user = user,
            failedTargetText = failedTargetText)

        article.title = requestDto.title
        article.content = requestDto.content

        return ArticleWriteResponseDto(
            article, article.user!!.email)
    }

    @Transactional
    fun delete(
        articleId: Long,
        requestDto: DeleteRequestDto,
        currentURI: String) {

        val failedTargetText = "${FailedTargetType.ARTICLE} ${FailedMethod.DELETE}"

        val user = existenceChecker.checkUserAccount(
            currentURI = currentURI,
            email = requestDto.email,
            password = requestDto.password,
            failedTargetText = failedTargetText,
        )

        val article = existenceChecker.checkArticle(
            articleId, failedTargetText, currentURI
        )

        ownershipChecker.check(
            target = article,
            currentURI = currentURI,
            user = user,
            failedTargetText = failedTargetText)

        articleRepository.delete(article)
    }
}