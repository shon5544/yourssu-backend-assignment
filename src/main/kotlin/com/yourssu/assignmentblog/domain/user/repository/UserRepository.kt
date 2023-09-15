package com.yourssu.assignmentblog.domain.user.repository

import com.yourssu.assignmentblog.domain.user.domain.User

interface UserRepository {
    fun findByEmail(email: String): User?
    fun findByRefreshToken(refreshToken: String): User?
    fun save(user: User): User
    fun delete(user: User)
}