package com.yourssu.assignmentblog.global.common.domain

import com.yourssu.assignmentblog.domain.user.domain.User
import com.yourssu.assignmentblog.global.common.entity.EntityWithOwnership
import com.yourssu.assignmentblog.global.common.repository.Repository
import com.yourssu.assignmentblog.global.error.exception.CustomException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class OwnershipChecker(
    private val repository: Repository
) {

    private fun findById(
        id: Long,
        currentURI: String): EntityWithOwnership {
        return repository.findById(id)
            ?: throw CustomException(
                status = HttpStatus.BAD_REQUEST,
                message = "게시글 수정 실패: 해당 Id를 가진 게시글이 없습니다.",
                requestURI = currentURI
            )
    }

    fun check(
        id: Long,
        currentURI: String,
        user: User,
        failedTarget: String
    ): EntityWithOwnership {
        val target = findById(id, currentURI)

        if (target.user != user)
            throw CustomException(
                status = HttpStatus.BAD_REQUEST,
                message = "$failedTarget 실패: 해당 게시글은 해당 유저의 소유가 아닙니다.",
                requestURI = currentURI
            )
        
        return target
    }
}