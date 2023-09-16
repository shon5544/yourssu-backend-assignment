package com.yourssu.assignmentblog.domain.article.service

import com.yourssu.assignmentblog.domain.article.domain.Article
import com.yourssu.assignmentblog.domain.article.dto.request.ArticleDeleteRequestDto
import com.yourssu.assignmentblog.domain.article.dto.request.ArticleWriteRequestDto
import com.yourssu.assignmentblog.domain.article.dto.response.ArticleWriteResponseDto
import com.yourssu.assignmentblog.domain.article.repository.ArticleRepository
import com.yourssu.assignmentblog.global.common.domain.OwnershipChecker
import com.yourssu.assignmentblog.global.common.domain.UserChecker
import com.yourssu.assignmentblog.global.common.enums.FailedMethod
import com.yourssu.assignmentblog.global.common.enums.FailedTargetType
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class ArticleService(
    private val articleRepository: ArticleRepository,
    private val userChecker: UserChecker,
    private val ownershipChecker: OwnershipChecker
) {

    @Transactional
    fun write(requestDto: ArticleWriteRequestDto, currentURI: String): ArticleWriteResponseDto {

        val user = userChecker.check(
            currentURI = currentURI,
            email = requestDto.email,
            password = requestDto.password,
            failedTarget = "${FailedTargetType.ARTICLE} ${FailedMethod.WRITE}",
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
        requestDto: ArticleWriteRequestDto
        ): ArticleWriteResponseDto {

        val user = userChecker.check(
            currentURI = currentURI,
            email = requestDto.email,
            password = requestDto.password,
            failedTarget = FailedTargetType.ARTICLE,
        )

        val article = ownershipChecker.check(
            id = articleId,
            currentURI = currentURI,
            user = user,
            failedTarget = "${FailedTargetType.ARTICLE} ${FailedMethod.EDIT}"
        ) as Article

        article.title = requestDto.title
        article.content = requestDto.content

        return ArticleWriteResponseDto(
            article, article.user!!.email)
    }

    @Transactional
    fun delete(
        articleId: Long,
        requestDto: ArticleDeleteRequestDto,
        currentURI: String) {

        val user = userChecker.check(
            currentURI = currentURI,
            email = requestDto.email,
            password = requestDto.password,
            failedTarget = FailedTargetType.ARTICLE,
        )

        val article = ownershipChecker.check(
            id = articleId,
            currentURI = currentURI,
            user = user,
            failedTarget = "${FailedTargetType.ARTICLE} ${FailedMethod.DELETE}"
        ) as Article

        articleRepository.delete(article)
    }
}