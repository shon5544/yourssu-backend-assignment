package com.yourssu.assignmentblog.global.common.domain

import com.yourssu.assignmentblog.domain.user.domain.User
import com.yourssu.assignmentblog.global.common.entity.EntityWithOwnership
import com.yourssu.assignmentblog.global.error.exception.CustomException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class OwnershipChecker {

    fun check(
        target: EntityWithOwnership,
        currentURI: String,
        user: User,
        failedTargetText: String
    ) {

        if (target.user != user)
            throw CustomException(
                status = HttpStatus.BAD_REQUEST,
                message = "$failedTargetText 실패: 해당 ${failedTargetText.split(" ")[0]}은 해당 유저의 소유가 아닙니다.",
                requestURI = currentURI)
    }
}