package com.yourssu.assignmentblog.global.common.domain

import com.yourssu.assignmentblog.domain.article.domain.Article
import com.yourssu.assignmentblog.domain.article.repository.ArticleRepository
import com.yourssu.assignmentblog.domain.comment.domain.Comment
import com.yourssu.assignmentblog.domain.comment.repository.CommentRepository
import com.yourssu.assignmentblog.domain.user.domain.User
import com.yourssu.assignmentblog.domain.user.repository.UserRepository
import com.yourssu.assignmentblog.global.error.exception.CustomException
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class ExistenceChecker(
    private val userRepository: UserRepository,
    private val articleRepository: ArticleRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val commentRepository: CommentRepository
) {
    fun checkUser(
        currentURI: String,
        email: String,
        password: String,
        failedTarget: String,
    ): User {
        val user = (userRepository.findByEmail(email)
            ?: throw CustomException(
                status = HttpStatus.BAD_REQUEST,
                message = "$failedTarget 실패: 전달받은 email에 해당하는 유저가 없습니다.",
                requestURI = currentURI
            ))

        if (!passwordEncoder.matches(password, user.password)) {
            throw CustomException(
                status = HttpStatus.BAD_REQUEST,
                message = "$failedTarget 실패: 비밀번호가 일치하지 않습니다.",
                requestURI = currentURI
            )
        }

        return user
    }

    fun checkArticle(
        articleId: Long,
        failedTarget: String,
        currentURI: String
    ): Article {
        return (articleRepository.findById(articleId)
            ?: throw CustomException(
                status = HttpStatus.BAD_REQUEST,
                message = "$failedTarget 실패: 존재하지 않는 ${failedTarget.split(" ")[0]}입니다.",
                requestURI = currentURI
            ))
    }

    fun checkComment(
        commentId: Long,
        failedTarget: String,
        currentURI: String
    ): Comment {
       return (commentRepository.findById(commentId)
           ?: throw CustomException(
               status = HttpStatus.BAD_REQUEST,
               message = "$failedTarget 실패: 존재하지 않는 ${failedTarget.split(" ")[0]}입니다.",
               requestURI = currentURI
           ))
    }
}