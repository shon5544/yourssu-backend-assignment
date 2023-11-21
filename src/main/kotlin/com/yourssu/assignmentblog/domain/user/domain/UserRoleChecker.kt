package com.yourssu.assignmentblog.domain.user.domain

import com.yourssu.assignmentblog.domain.user.repository.UserRepository
import java.lang.IllegalArgumentException

class UserRoleChecker {
    companion object {
        fun <T> checkRoleWithRepository(
            repository: UserRepository,
            email: String,
            function: () -> T,
        ): T {
            repository.findByEmail(email) ?: throw IllegalArgumentException("유저 정보 조회 실패: 요청을 처리할 수 있는 권한이 없습니다.")

            return function.invoke()
        }
    }
}
