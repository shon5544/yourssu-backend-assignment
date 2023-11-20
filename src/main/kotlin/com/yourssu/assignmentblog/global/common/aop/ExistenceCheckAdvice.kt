package com.yourssu.assignmentblog.global.common.aop

import com.yourssu.assignmentblog.domain.article.repository.ArticleRepository
import com.yourssu.assignmentblog.domain.comment.repository.CommentRepository
import com.yourssu.assignmentblog.domain.user.repository.UserRepository
import com.yourssu.assignmentblog.global.error.exception.CustomException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class ExistenceCheckAdvice {
    companion object {
        fun <T> checkUserAccount(
            currentURI: String,
            email: String,
            failedTargetText: String,
            userRepository: UserRepository,
            function: () -> T,
        ): T {
            if (userRepository.findByEmail(email) == null) {
                throw CustomException(
                    status = HttpStatus.BAD_REQUEST,
                    message = "$failedTargetText 실패: 전달받은 email에 해당하는 유저가 없습니다.",
                    requestURI = currentURI,
                )
            }

            return function.invoke()
        }

        fun <T> checkArticleExistence(
            articleId: Long,
            currentURI: String,
            failedTargetText: String,
            articleRepository: ArticleRepository,
            function: () -> T,
        ): T {
            if (articleRepository.findById(articleId) == null) {
                throw CustomException(
                    status = HttpStatus.BAD_REQUEST,
                    message = "$failedTargetText 실패: 존재하지 않는 ${failedTargetText.split(" ")[0]}입니다.",
                    requestURI = currentURI,
                )
            }

            return function.invoke()
        }

        fun <T> checkCommentExistence(
            commentId: Long,
            currentURI: String,
            failedTargetText: String,
            commentRepository: CommentRepository,
            function: () -> T,
        ): T {
            if (commentRepository.findById(commentId) == null) {
                throw CustomException(
                    status = HttpStatus.BAD_REQUEST,
                    message = "$failedTargetText 실패: 존재하지 않는 ${failedTargetText.split(" ")[0]}입니다.",
                    requestURI = currentURI,
                )
            }

            return function.invoke()
        }

        fun <T> checkUserEmailNotExist(
            email: String,
            currentURI: String,
            userRepository: UserRepository,
            function: () -> T,
        ): T {
            if (userRepository.findByEmail(email) != null) {
                throw CustomException(
                    status = HttpStatus.BAD_REQUEST,
                    // 유저가 존재할 때 Exception을 날리는 상황은 회원가입 말고는 없음.
                    message = "회원 가입 실패: 이미 존재하는 유저입니다.",
                    requestURI = currentURI,
                )
            }

            return function.invoke()
        }
    }
}
