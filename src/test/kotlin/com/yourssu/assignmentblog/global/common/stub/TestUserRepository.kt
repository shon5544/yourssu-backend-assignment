package com.yourssu.assignmentblog.global.common.stub

import com.yourssu.assignmentblog.domain.user.domain.User
import com.yourssu.assignmentblog.domain.user.dto.request.GetUsersRequestDto
import com.yourssu.assignmentblog.domain.user.repository.UserRepository

class TestUserRepository : UserRepository {
    private var user: User? = null

    override fun findByEmail(email: String): User? {
        if (email == "yourssu@gmail.com") {
            return user
        }

        return null
    }

    override fun findByRefreshToken(refreshToken: String): User {
        return User(
            id = 1,
            email = "yourssu@gmail.com",
            password = "asdf",
            username = "beomsu son",
            // invalid refresh token
            refreshToken = "aldjkflkadjfkjlkdsjfasdkjf",
        )
    }

    override fun save(user: User): User {
        this.user = user
        return user
    }

    override fun delete(user: User) {
        return
    }

    override fun findUsers(userDto: GetUsersRequestDto): List<User>? {
        TODO("이 메서드는 다른 객체에서 검토될 예정입니다.")
    }
}
