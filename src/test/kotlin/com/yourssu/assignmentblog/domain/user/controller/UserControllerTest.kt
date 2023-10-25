package com.yourssu.assignmentblog.domain.user.controller

import com.yourssu.assignmentblog.domain.article.repository.ArticleRepository
import com.yourssu.assignmentblog.domain.comment.repository.CommentRepository
import com.yourssu.assignmentblog.domain.user.domain.User
import com.yourssu.assignmentblog.domain.user.dto.request.SignupRequestDto
import com.yourssu.assignmentblog.domain.user.dto.response.SignupResponseDto
import com.yourssu.assignmentblog.domain.user.repository.UserRepository
import com.yourssu.assignmentblog.domain.user.service.UserService
import com.yourssu.assignmentblog.global.auth.jwt.AuthInfo
import com.yourssu.assignmentblog.global.common.domain.ExistenceChecker
import com.yourssu.assignmentblog.global.common.stub.TestArticleRepository
import com.yourssu.assignmentblog.global.common.stub.TestCommentRepository
import com.yourssu.assignmentblog.global.common.stub.TestUserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@DisplayName("UserController 테스트")
internal class UserControllerTest {

    companion object {
        private lateinit var userRepository: UserRepository
        private lateinit var articleRepository: ArticleRepository
        private lateinit var commentRepository: CommentRepository

        private val passwordEncoder = BCryptPasswordEncoder()

        private val user: User = User(
            id = 1,
            email = "yourssu@gmail.com",
            password = passwordEncoder.encode("asdj"),
            username = "beomsu son"
        )

        private lateinit var existenceChecker: ExistenceChecker

        private lateinit var userService: UserService

        private lateinit var userController: UserController

        @BeforeAll
        @JvmStatic
        fun initialize() {
            userRepository = TestUserRepository()
            articleRepository = TestArticleRepository()
            commentRepository = TestCommentRepository()

            existenceChecker = ExistenceChecker(
                userRepository = userRepository,
                articleRepository = articleRepository,
                commentRepository = commentRepository
            )

            userService = UserService(
                userRepository = userRepository,
                existenceChecker = existenceChecker,
                passwordEncoder = passwordEncoder,
            )

            userController = UserController(userService)
        }
    }

    @BeforeEach
    fun initializeEach() {

        userRepository = TestUserRepository()

        existenceChecker = ExistenceChecker(
            userRepository = userRepository,
            articleRepository = articleRepository,
            commentRepository = commentRepository
        )

        userService = UserService(
            userRepository = userRepository,
            passwordEncoder = passwordEncoder,
            existenceChecker = existenceChecker
        )

        userController = UserController(userService)
    }

    @Test
    @DisplayName("signup 테스트")
    fun signup() {
        // given
        val requestDto = SignupRequestDto(
            email = "yourssu@gmail.com",
            password = "asdj",
            username = "beomsu son",
            role = "USER"
        )

        // when
        val result = userController.signup(requestDto)

        // then
        val expectedResult = SignupResponseDto(user)
        assertEquals(expectedResult, result)
    }

    @Test
    @DisplayName("withdraw 테스트")
    fun withdraw() {

        userRepository.save(user)

        // given
        val email = "yourssu@gmail.com"

        // when-then
        userController.withdraw(authInfo = AuthInfo(email))
    }
}