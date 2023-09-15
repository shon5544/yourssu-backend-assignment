package com.yourssu.assignmentblog.domain.user.repository.impl

import com.yourssu.assignmentblog.domain.user.domain.User
import com.yourssu.assignmentblog.domain.user.repository.UserRepository

class TestUserRepository: UserRepository {

    private val user: User = User(
        id = 1,
        email = "yourssu@gmail.com",
        password = "asdj",
        username = "beomsu son"
    )

    override fun findByEmail(email: String): User {
        return user
    }

    override fun findByRefreshToken(refreshToken: String): User {
        return User(
            id = 1,
            email = "yourssu@gmail.com",
            password = "asdf",
            username = "beomsu son",
            // invalid refresh token
            refreshToken = "aldjkflkadjfkjlkdsjfasdkjf"
        )
    }

    override fun save(user: User): User {
        return user
    }

    override fun delete(user: User) {
        return
    }

}