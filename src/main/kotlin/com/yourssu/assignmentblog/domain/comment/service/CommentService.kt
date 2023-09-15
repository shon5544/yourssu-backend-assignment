package com.yourssu.assignmentblog.domain.comment.service

import com.yourssu.assignmentblog.domain.article.repository.ArticleRepository
import com.yourssu.assignmentblog.domain.comment.domain.Comment
import com.yourssu.assignmentblog.domain.comment.dto.request.CommentWriteRequestDto
import com.yourssu.assignmentblog.domain.comment.dto.response.CommentWriteResponseDto
import com.yourssu.assignmentblog.domain.comment.repository.CommentRepository
import com.yourssu.assignmentblog.domain.user.repository.UserRepository
import com.yourssu.assignmentblog.global.common.exception.CustomException
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository,
    private val articleRepository: ArticleRepository,
    private val passwordEncoder: BCryptPasswordEncoder
) {

    @Transactional
    fun write(
        articleId: Long,
        currentURI: String,
        requestDto: CommentWriteRequestDto): CommentWriteResponseDto {
        val user = (userRepository.findByEmail(requestDto.email)
            ?: throw CustomException(
                status = HttpStatus.BAD_REQUEST,
                message = "댓글 작성 실패: 해당 email에 해당하는 유저가 없습니다.",
                requestURI = currentURI
            ))

        if (!passwordEncoder.matches(requestDto.password, user.password)) {
            throw CustomException(
                status = HttpStatus.BAD_REQUEST,
                message = "댓글 작성 실패: 비밀번호가 일치하지 않습니다.",
                requestURI = currentURI
            )
        }

        val article = (articleRepository.findById(articleId)
            ?: throw CustomException(
                status = HttpStatus.BAD_REQUEST,
                message = "댓글 작성 실패: 존재하지 않는 게시물입니다.",
                requestURI = currentURI
            ))

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
        val user = (userRepository.findByEmail(requestDto.email)
            ?: throw CustomException(
                status = HttpStatus.BAD_REQUEST,
                message = "댓글 수정 실패: 해당 email에 해당하는 유저가 없습니다.",
                requestURI = currentURI
            ))

        if (!passwordEncoder.matches(requestDto.password, user.password)) {
            throw CustomException(
                status = HttpStatus.BAD_REQUEST,
                message = "댓글 수정 실패: 비밀번호가 일치하지 않습니다.",
                requestURI = currentURI
            )
        }

        val comment = commentRepository.findById(commentId)
            ?: throw CustomException(
                status = HttpStatus.BAD_REQUEST,
                message = "댓글 수정 실패: 존재하지 않는 게시물입니다.",
                requestURI = currentURI
            )

        comment.content = requestDto.content

        return CommentWriteResponseDto(
            comment = comment,
            email = user.email
        )
    }
}