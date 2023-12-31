package com.yourssu.assignmentblog.global.common.domain

import com.yourssu.assignmentblog.domain.article.domain.Article
import com.yourssu.assignmentblog.domain.article.repository.ArticleRepository
import com.yourssu.assignmentblog.domain.comment.domain.Comment
import com.yourssu.assignmentblog.domain.comment.repository.CommentRepository
import com.yourssu.assignmentblog.domain.user.domain.User
import com.yourssu.assignmentblog.domain.user.repository.UserRepository
import com.yourssu.assignmentblog.global.error.exception.CustomException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class ExistenceChecker(
    private val userRepository: UserRepository,
    private val articleRepository: ArticleRepository,
//    private val passwordEncoder: BCryptPasswordEncoder,
    private val commentRepository: CommentRepository,
) {
    fun checkUserAccount(
        currentURI: String,
        email: String,
//        password: String,
        failedTargetText: String,
    ): User {
//        checkUserPassword(
//            password = password,
//            user = user,
//            failedTargetText = failedTargetText,
//            currentURI = currentURI
//        )

        return checkUserEmailExist(
            email = email,
            failedTargetText = failedTargetText,
            currentURI = currentURI,
        )
    }

    fun checkUserEmailExist(
        email: String,
        failedTargetText: String,
        currentURI: String,
    ): User {
        return userRepository.findByEmail(email)
            ?: throw CustomException(
                status = HttpStatus.BAD_REQUEST,
                message = "$failedTargetText 실패: 전달받은 email에 해당하는 유저가 없습니다.",
                requestURI = currentURI,
            )
    }

//    fun checkUserPassword(
//        password: String,
//        user: User,
//        failedTargetText: String,
//        currentURI: String
//    ) {
//        if (!passwordEncoder.matches(password, user.password)) {
//            throw CustomException(
//                status = HttpStatus.BAD_REQUEST,
//                message = "$failedTargetText 실패: 비밀번호가 일치하지 않습니다.",
//                requestURI = currentURI
//            )
//        }
//    }

    fun checkUserEmailNotExist(
        email: String,
        currentURI: String,
    ) {
        if (userRepository.findByEmail(email) != null) {
            throw CustomException(
                status = HttpStatus.BAD_REQUEST,
                // 유저가 존재할 때 Exception을 날리는 상황은 회원가입 말고는 없음.
                message = "회원 가입 실패: 이미 존재하는 유저입니다.",
                requestURI = currentURI,
            )
        }
    }

    fun checkArticle(
        articleId: Long,
        failedTargetText: String,
        currentURI: String,
    ): Article {
        return (
            articleRepository.findById(articleId)
                ?: throw CustomException(
                    status = HttpStatus.BAD_REQUEST,
                    message = "$failedTargetText 실패: 존재하지 않는 ${failedTargetText.split(" ")[0]}입니다.",
                    requestURI = currentURI,
                )
            )
    }

    fun checkComment(
        commentId: Long,
        failedTarget: String,
        currentURI: String,
    ): Comment {
        return (
            commentRepository.findById(commentId)
                ?: throw CustomException(
                    status = HttpStatus.BAD_REQUEST,
                    message = "$failedTarget 실패: 존재하지 않는 ${failedTarget.split(" ")[0]}입니다.",
                    requestURI = currentURI,
                )
            )
    }
}
