package com.yourssu.assignmentblog.domain.comment.service

import com.yourssu.assignmentblog.domain.comment.domain.Comment
import com.yourssu.assignmentblog.domain.comment.dto.request.CommentDeleteRequestDto
import com.yourssu.assignmentblog.domain.comment.dto.request.CommentWriteRequestDto
import com.yourssu.assignmentblog.domain.comment.dto.response.CommentWriteResponseDto
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
        requestDto: CommentWriteRequestDto): CommentWriteResponseDto {

        val failedTargetText = "${FailedTargetType.COMMENT} ${FailedMethod.WRITE}"

        val user = existenceChecker.checkUser(
            currentURI = currentURI,
            email = requestDto.email,
            password = requestDto.password,
            failedTargetText = failedTargetText
        )

        val article = existenceChecker.checkArticle(
            articleId = articleId,
            currentURI = currentURI,
            failedTarget = failedTargetText
        )

        val comment = Comment(
            content = requestDto.content,
            user = user,
            article = article
        )

        return CommentWriteResponseDto(
            comment = commentRepository.save(comment),
            email = user.email
        )
    }

    @Transactional
    fun edit(
        articleId: Long,
        commentId: Long,
        currentURI: String,
        requestDto: CommentWriteRequestDto
    ): CommentWriteResponseDto {

        val failedTargetText = "${FailedTargetType.COMMENT} ${FailedMethod.EDIT}"

        val user = existenceChecker.checkUser(
            currentURI = currentURI,
            email = requestDto.email,
            password = requestDto.password,
            failedTargetText = failedTargetText
        )

        val comment = existenceChecker.checkComment(
            commentId = commentId,
            failedTarget = failedTargetText,
            currentURI = currentURI
        )

        comment.content = requestDto.content

        return CommentWriteResponseDto(
            comment = comment,
            email = user.email
        )
    }

    @Transactional
    fun delete(
        commentId: Long,
        currentURI: String,
        requestDto: CommentDeleteRequestDto) {

        val failedTargetText = "${FailedTargetType.COMMENT} ${FailedMethod.DELETE}"

        val user = existenceChecker.checkUser(
            currentURI = currentURI,
            email = requestDto.email,
            password = requestDto.password,
            failedTargetText = failedTargetText
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
            failedTarget = failedTargetText
        )

        commentRepository.delete(comment)
    }
}