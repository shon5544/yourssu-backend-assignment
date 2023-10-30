package com.yourssu.assignmentblog.global.common.aop

import com.yourssu.assignmentblog.global.common.domain.ExistenceChecker
import org.springframework.stereotype.Component

@Component
class ExistenceCheckAdviceHolder(
    _existenceCheckAdvice: ExistenceCheckAdvice
) {
    init {
        existenceCheckAdvice = _existenceCheckAdvice
    }

    companion object {
        private lateinit var existenceCheckAdvice: ExistenceCheckAdvice

        fun <T> checkUserAccount(
            currentURI: String,
            email: String,
            failedTargetText: String,
            function: () -> T
        ): T {
            return existenceCheckAdvice.checkUserAccount(
                currentURI,
                email,
                failedTargetText,
                function
            )
        }

        fun <T> checkArticleExistence(
            articleId: Long,
            currentURI: String,
            failedTargetText: String,
            function: () -> T
        ): T {
            return existenceCheckAdvice.checkArticleExistence(
                articleId,
                currentURI,
                failedTargetText,
                function
            )
        }

        fun <T> checkCommentExistence(
            commentId: Long,
            currentURI: String,
            failedTargetText: String,
            function: () -> T
        ): T {
            return existenceCheckAdvice.checkCommentExistence(
                commentId,
                currentURI,
                failedTargetText,
                function
            )
        }

        fun <T> checkUserEmailNotExist(
            email: String,
            currentURI: String,
            function: () -> T
        ): T {

            return existenceCheckAdvice.checkUserEmailNotExist(email, currentURI, function)
        }
    }

    @Component
    class ExistenceCheckAdvice(
        private val existenceChecker: ExistenceChecker
    ) {

        fun <T> checkUserAccount(
            currentURI: String,
            email: String,
            failedTargetText: String,
            function: () -> T
        ): T {

            existenceChecker.checkUserAccount(
                currentURI,
                email,
                failedTargetText
            )

            return function.invoke()
        }

        fun <T> checkArticleExistence(
            articleId: Long,
            currentURI: String,
            failedTargetText: String,
            function: () -> T
        ): T {

            existenceChecker.checkArticle(
                articleId,
                failedTargetText,
                currentURI
            )

            return function.invoke()
        }

        fun <T> checkCommentExistence(
            commentId: Long,
            currentURI: String,
            failedTargetText: String,
            function: () -> T
        ): T {

            existenceChecker.checkComment(
                commentId,
                failedTargetText,
                currentURI
            )

            return function.invoke()
        }

        fun <T> checkUserEmailNotExist(
            email: String,
            currentURI: String,
            function: () -> T
        ): T {

            existenceChecker.checkUserEmailNotExist(email, currentURI)

            return function.invoke()
        }
    }
}