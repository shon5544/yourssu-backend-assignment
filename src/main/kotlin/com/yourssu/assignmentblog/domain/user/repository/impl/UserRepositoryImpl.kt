package com.yourssu.assignmentblog.domain.user.repository.impl

import com.yourssu.assignmentblog.domain.user.domain.User
import com.yourssu.assignmentblog.domain.user.repository.UserRepository
import com.yourssu.assignmentblog.domain.user.repository.jpa.UserJpaRepository
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
    private val userJpaRepository: UserJpaRepository
): UserRepository {

    override fun findByEmail(email: String): User? {
        return userJpaRepository.findByEmail(email)
    }

    override fun findByRefreshToken(refreshToken: String): User? {
        return userJpaRepository.findByRefreshToken(refreshToken)
    }

    override fun save(user: User): User {
        return userJpaRepository.save(user)
    }

    override fun delete(user: User) {
        userJpaRepository.delete(user)
    }

}