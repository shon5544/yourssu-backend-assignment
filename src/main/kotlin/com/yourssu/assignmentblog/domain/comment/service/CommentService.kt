package com.yourssu.assignmentblog.domain.comment.service

import com.yourssu.assignmentblog.domain.comment.domain.Comment
import com.yourssu.assignmentblog.domain.comment.dto.request.CommentRequestDto
import com.yourssu.assignmentblog.domain.comment.dto.response.CommentResponseDto
import com.yourssu.assignmentblog.domain.comment.repository.CommentRepository
import com.yourssu.assignmentblog.global.common.domain.ExistenceChecker
import com.yourssu.assignmentblog.global.common.domain.OwnershipChecker
import com.yourssu.assignmentblog.global.common.enums.FailedMethod
import com.yourssu.assignmentblog.global.common.enums.FailedTargetType
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val existenceChecker: ExistenceChecker,
    private val ownershipChecker: OwnershipChecker
) {

    @Transactional
    fun write(
        articleId: Long,
        currentURI: String,
        requestDto: CommentRequestDto,
        email: String
    ): CommentResponseDto {

        val failedTargetText = "${FailedTargetType.COMMENT} ${FailedMethod.WRITE}"

        val user = existenceChecker.checkUserAccount(
            currentURI = currentURI,
            email = email,
            failedTargetText = failedTargetText
        )

        val article = existenceChecker.checkArticle(
            articleId = articleId,
            currentURI = currentURI,
            failedTargetText = failedTargetText
        )

        val comment = Comment(
            content = requestDto.content,
            user = user,
            article = article
        )

        return CommentResponseDto(
            comment = commentRepository.save(comment),
            email = user.email
        )
    }

    @Transactional
    fun edit(
        articleId: Long,
        commentId: Long,
        currentURI: String,
        requestDto: CommentRequestDto,
        email: String
    ): CommentResponseDto {

        val failedTargetText = "${FailedTargetType.COMMENT} ${FailedMethod.EDIT}"

        val user = existenceChecker.checkUserAccount(
            currentURI = currentURI,
            email = email,
            failedTargetText = failedTargetText
        )

        existenceChecker.checkArticle(
            articleId = articleId,
            failedTargetText = failedTargetText,
            currentURI = currentURI
        )

        val comment = existenceChecker.checkComment(
            commentId = commentId,
            failedTarget = failedTargetText,
            currentURI = currentURI
        )

        ownershipChecker.check(
            target = comment,
            currentURI = currentURI,
            user = user,
            failedTargetText = failedTargetText
        )

        comment.content = requestDto.content

        return CommentResponseDto(
            comment = comment,
            email = user.email
        )
    }

    @Transactional
    fun delete(
        articleId: Long,
        commentId: Long,
        currentURI: String,
        email: String
    ) {

        val failedTargetText = "${FailedTargetType.COMMENT} ${FailedMethod.DELETE}"

        val user = existenceChecker.checkUserAccount(
            currentURI = currentURI,
            email = email,
            failedTargetText = failedTargetText
        )

        existenceChecker.checkArticle(
            articleId = articleId,
            failedTargetText = failedTargetText,
            currentURI = currentURI
        )

        val comment = existenceChecker.checkComment(
            commentId =  commentId,
            failedTarget = failedTargetText,
            currentURI = currentURI
        )

        ownershipChecker.check(
            target = comment,
            currentURI = currentURI,
            user = user,
            failedTargetText = failedTargetText
        )

        commentRepository.delete(comment)
    }
}