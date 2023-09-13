package com.yourssu.assignmentblog.domain.user.repository.jpa

import com.yourssu.assignmentblog.domain.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserJpaRepository: JpaRepository<User, Long?> {
    fun findByEmail(email: String): User?
    fun findByRefreshToken(refreshToken: String): User?
}