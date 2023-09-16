package com.yourssu.assignmentblog.global.common.domain

import com.yourssu.assignmentblog.domain.user.domain.User
import com.yourssu.assignmentblog.domain.user.repository.UserRepository
import com.yourssu.assignmentblog.global.error.exception.CustomException
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class UserChecker(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder
) {
    fun check(
        currentURI: String,
        email: String,
        password: String,
        failedTarget: String,
    ): User {
        val user = (userRepository.findByEmail(email)
            ?: throw CustomException(
                status = HttpStatus.BAD_REQUEST,
                message = "$failedTarget 실패: 해당 email에 해당하는 유저가 없습니다.",
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
}