package com.yourssu.assignmentblog.domain.user.service

import com.yourssu.assignmentblog.domain.article.repository.ArticleRepository
import com.yourssu.assignmentblog.domain.comment.repository.CommentRepository
import com.yourssu.assignmentblog.domain.user.domain.User
import com.yourssu.assignmentblog.domain.user.dto.request.SignupRequestDto
import com.yourssu.assignmentblog.domain.user.dto.response.SignupResponseDto
import com.yourssu.assignmentblog.domain.user.repository.UserRepository
import com.yourssu.assignmentblog.global.common.domain.ExistenceChecker
import com.yourssu.assignmentblog.global.common.dto.DeleteRequestDto
import com.yourssu.assignmentblog.global.common.stub.TestArticleRepository
import com.yourssu.assignmentblog.global.common.stub.TestCommentRepository
import com.yourssu.assignmentblog.global.common.stub.TestUserRepository
import com.yourssu.assignmentblog.global.common.uri.RequestURI
import com.yourssu.assignmentblog.global.error.exception.CustomException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@DisplayName("UserService 테스트")
internal class UserServiceTest {

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

        const val SIGNUP = "${RequestURI.USER}/signup"
        const val WITHDRAW = "${RequestURI.USER}/withdraw"

        @BeforeAll
        @JvmStatic
        fun initialize() {
            userRepository = TestUserRepository()
            articleRepository = TestArticleRepository()
            commentRepository = TestCommentRepository()

            existenceChecker = ExistenceChecker(
                userRepository = userRepository,
                articleRepository = articleRepository,
                passwordEncoder = passwordEncoder,
                commentRepository = commentRepository
            )

            userService = UserService(
                userRepository = userRepository,
                existenceChecker = existenceChecker,
                passwordEncoder = passwordEncoder,
            )

            userRepository.save(user)
        }
    }

    @BeforeEach
    fun initializeEach() {

        val user = User(
            id = 1,
            email = "yourssu@gmail.com",
            password = passwordEncoder.encode("asdj"),
            username = "beomsu son"
        )

        userRepository.save(user)

        existenceChecker = ExistenceChecker(
            userRepository = userRepository,
            articleRepository = articleRepository,
            passwordEncoder = passwordEncoder,
            commentRepository = commentRepository
        )

        userService = UserService(
            userRepository = userRepository,
            passwordEncoder = passwordEncoder,
            existenceChecker = existenceChecker
        )
    }

    @Nested
    @DisplayName("signup 메서드를 사용할 때")
    inner class DescribeSignup {

        @Nested
        @DisplayName("이미 동일 email로 가입한 기록이 있는 경우")
        inner class ContextDuplicatedEmail {

            @Test
            @DisplayName("CustomException이 발생된다")
            fun it_throw_customException() {
                // given
                val requestDto = SignupRequestDto(
                    email = "yourssu@gmail.com",
                    password = "asdj",
                    username = "beomsu"
                )

                // when-then
                assertThrows(CustomException::class.java) {
                    userService.signup(
                        requestDto = requestDto,
                        currentURI = SIGNUP
                    )
                }
            }

        }

        @Nested
        @DisplayName("아예 새로 등록하는 유저일 경우")
        inner class ContextNotDuplicatedEmail {

            @Test
            @DisplayName("성공적으로 SignupResponseDto를 반환한다.")
            fun it_return_signupResponseDto() {

                val userRepositoryForDuplicateTest = TestUserRepository()

                existenceChecker = ExistenceChecker(
                    userRepository = userRepositoryForDuplicateTest,
                    articleRepository = articleRepository,
                    passwordEncoder = passwordEncoder,
                    commentRepository = commentRepository
                )

                userService = UserService(
                    userRepository = userRepository,
                    passwordEncoder = passwordEncoder,
                    existenceChecker = existenceChecker
                )

                // given
                val requestDto = SignupRequestDto(
                    email = "yourssu@gmail.com",
                    password = "asdj",
                    username = "beomsu son"
                )

                // when
                val result = userService.signup(
                    requestDto = requestDto,
                    currentURI = SIGNUP
                )

                // then
                val expectedResult = SignupResponseDto(user)
                assertEquals(expectedResult, result)
            }
        }

    }

    @Nested
    @DisplayName("withdraw 메서드를 사용할 때")
    inner class DescribeWithdraw {

        @Nested
        @DisplayName("유저가 실제 존재하는 유저인지 확인한다.")
        inner class ContextUserExist {

            @Nested
            @DisplayName("만약 존재하지 않는 유저라면")
            inner class ContextNotExist {

                @Test
                @DisplayName("CustomException이 발생된다")
                fun it_throws_customException() {

                    val userRepositoryForDuplicateTest = TestUserRepository()

                    existenceChecker = ExistenceChecker(
                        userRepository = userRepositoryForDuplicateTest,
                        articleRepository = articleRepository,
                        passwordEncoder = passwordEncoder,
                        commentRepository = commentRepository
                    )

                    userService = UserService(
                        userRepository = userRepository,
                        passwordEncoder = passwordEncoder,
                        existenceChecker = existenceChecker
                    )

                    // given
                    val requestDto = DeleteRequestDto(
                        email = "yourssu@gmail.com",
                        password = "asdj"
                    )

                    // when-then
                    assertThrows(CustomException::class.java) {
                        userService.withdraw(
                            requestDto = requestDto,
                            currentURI = WITHDRAW
                        )
                    }
                }
            }

            @Nested
            @DisplayName("만약 존재하는 유저라면")
            inner class ContextExist {

                @Nested
                @DisplayName("만약 비밀번호가 틀렸다면")
                inner class ContextWrongPassword {

                    @Test
                    @DisplayName("CustomException이 발생된다.")
                    fun it_throws_customException() {
                        // given
                        val requestDto = DeleteRequestDto(
                            email = "yourssu@gmail.com",
                            password = "fffffff"
                        )

                        // when-then
                        assertThrows(CustomException::class.java) {
                            userService.withdraw(
                                requestDto = requestDto,
                                currentURI = WITHDRAW
                            )
                        }
                    }
                }

                @Nested
                @DisplayName("만약 비밀번호가 맞았다면")
                inner class ContextCorrectPassword {

                    @Test
                    @DisplayName("어떤 Exception 없이 잘 탈퇴된다.")
                    fun it_works_well() {
                        // given
                        val requestDto = DeleteRequestDto(
                            email = "yourssu@gmail.com",
                            password = "asdj"
                        )

                        // when-then
                        userService.withdraw(
                            requestDto = requestDto,
                            currentURI = WITHDRAW
                        )
                    }
                }
            }
        }
    }
}