package com.yourssu.assignmentblog.domain.comment.service

import com.yourssu.assignmentblog.domain.article.domain.Article
import com.yourssu.assignmentblog.domain.article.repository.ArticleRepository
import com.yourssu.assignmentblog.domain.comment.domain.Comment
import com.yourssu.assignmentblog.domain.comment.dto.request.CommentRequestDto
import com.yourssu.assignmentblog.domain.comment.dto.response.CommentResponseDto
import com.yourssu.assignmentblog.domain.comment.repository.CommentRepository
import com.yourssu.assignmentblog.domain.user.domain.User
import com.yourssu.assignmentblog.domain.user.repository.UserRepository
import com.yourssu.assignmentblog.global.common.aop.ExistenceCheckAdviceHolder
import com.yourssu.assignmentblog.global.common.aop.OwnershipCheckAdviceHolder
import com.yourssu.assignmentblog.global.common.enums.FailedMethod
import com.yourssu.assignmentblog.global.common.enums.FailedTargetType
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val articleRepository: ArticleRepository,
    private val userRepository: UserRepository
) {

    @Transactional
    fun write(
        articleId: Long,
        requestDto: CommentRequestDto,
        email: String
    ): CommentResponseDto = ExistenceCheckAdviceHolder.checkUserAccount(
        currentURI = requestDto.currentURI,
        email = email,
        failedTargetText = requestDto.failedTargetText
    ) {

        return@checkUserAccount ExistenceCheckAdviceHolder.checkArticleExistence(
            articleId = articleId,
            currentURI = requestDto.currentURI,
            failedTargetText = requestDto.failedTargetText
        ) {
            val article: Article = articleRepository.findById(articleId)!!
            val user: User = userRepository.findByEmail(email)!!

            val comment = Comment(
                content = requestDto.content,
                user = user,
                article = article
            )

            return@checkArticleExistence CommentResponseDto(
                comment = commentRepository.save(comment),
                email = user.email
            )
        }
    }

    @Transactional
    fun edit(
        articleId: Long,
        commentId: Long,
        requestDto: CommentRequestDto,
        email: String
    ): CommentResponseDto = ExistenceCheckAdviceHolder.checkUserAccount(
        currentURI = requestDto.currentURI,
        email = email,
        failedTargetText = requestDto.failedTargetText,
    ) {

        return@checkUserAccount ExistenceCheckAdviceHolder.checkArticleExistence(
            articleId = articleId,
            currentURI = requestDto.currentURI,
            failedTargetText = requestDto.failedTargetText
        ) {

            return@checkArticleExistence ExistenceCheckAdviceHolder.checkCommentExistence(
                commentId = commentId,
                currentURI = requestDto.currentURI,
                failedTargetText = requestDto.failedTargetText
            ) {
                val comment: Comment = commentRepository.findById(commentId)!!
                val user: User = userRepository.findByEmail(email)!!

                comment.update(requestDto, user)

                return@checkCommentExistence CommentResponseDto(
                    comment = comment,
                    email = user.email
                )
            }

        }
    }

    @Transactional
    fun delete(
        articleId: Long,
        commentId: Long,
        currentURI: String,
        email: String,
        failedTargetText: String = "${FailedTargetType.COMMENT} ${FailedMethod.DELETE}"
    ) = ExistenceCheckAdviceHolder.checkUserAccount(
        currentURI = currentURI,
        email = email,
        failedTargetText = failedTargetText
    ) {

        return@checkUserAccount ExistenceCheckAdviceHolder.checkArticleExistence(
            articleId = articleId,
            currentURI = currentURI,
            failedTargetText = failedTargetText
        ) {

            return@checkArticleExistence ExistenceCheckAdviceHolder.checkCommentExistence(
                commentId = commentId,
                currentURI = currentURI,
                failedTargetText = failedTargetText
            ) {

                val comment: Comment = commentRepository.findById(commentId)!!
                val user: User = userRepository.findByEmail(email)!!

                return@checkCommentExistence OwnershipCheckAdviceHolder.checkOwnership(
                    target = comment,
                    currentURI = currentURI,
                    user = user,
                    failedTargetText = failedTargetText
                ) {
                    commentRepository.delete(comment)
                }
            }
        }
    }
}