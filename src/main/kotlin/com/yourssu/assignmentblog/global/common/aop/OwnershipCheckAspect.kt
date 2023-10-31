package com.yourssu.assignmentblog.global.common.aop

import com.yourssu.assignmentblog.domain.user.domain.User
import com.yourssu.assignmentblog.global.common.domain.OwnershipChecker
import com.yourssu.assignmentblog.global.common.entity.EntityWithOwnership
import org.springframework.stereotype.Component

@Component
class OwnershipCheckAspect(
    _ownershipCheckAdvice: OwnershipCheckAdvice
) {

    init {
        ownershipCheckAdvice = _ownershipCheckAdvice
    }

    companion object {
        private lateinit var ownershipCheckAdvice: OwnershipCheckAdvice

        fun <T> checkOwnership(
            target: EntityWithOwnership,
            currentURI: String,
            user: User,
            failedTargetText: String,
            function: () -> T
        ): T {
            return ownershipCheckAdvice.checkOwnership(target, currentURI, user, failedTargetText, function)
        }
    }

    @Component
    class OwnershipCheckAdvice(
        private val ownershipChecker: OwnershipChecker
    ) {
        fun <T> checkOwnership(
            target: EntityWithOwnership,
            currentURI: String,
            user: User,
            failedTargetText: String,
            function: () -> T
        ): T {
            ownershipChecker.check(
                target = target,
                currentURI = currentURI,
                user = user,
                failedTargetText = failedTargetText
            )

            return function.invoke()
        }
    }
}