package com.yourssu.assignmentblog.domain.article.service

import com.yourssu.assignmentblog.domain.article.domain.Article
import com.yourssu.assignmentblog.domain.article.dto.request.ArticleRequestDto
import com.yourssu.assignmentblog.domain.article.dto.response.ArticleResponseDto
import com.yourssu.assignmentblog.domain.article.repository.ArticleRepository
import com.yourssu.assignmentblog.domain.comment.repository.CommentRepository
import com.yourssu.assignmentblog.domain.user.domain.User
import com.yourssu.assignmentblog.domain.user.repository.UserRepository
import com.yourssu.assignmentblog.global.common.stub.TestArticleRepository
import com.yourssu.assignmentblog.global.common.stub.TestCommentRepository
import com.yourssu.assignmentblog.global.common.stub.TestUserRepository
import com.yourssu.assignmentblog.global.common.domain.ExistenceChecker
import com.yourssu.assignmentblog.global.common.domain.OwnershipChecker
import com.yourssu.assignmentblog.global.common.uri.RequestURI
import com.yourssu.assignmentblog.global.error.exception.CustomException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@DisplayName("ArticleService 테스트")
internal class ArticleServiceTest {

    companion object {
        private lateinit var userRepository: UserRepository
        private lateinit var articleRepository: ArticleRepository
        private lateinit var commentRepository: CommentRepository

        private val passwordEncoder = BCryptPasswordEncoder()
        private val ownershipChecker = OwnershipChecker()

        private val user: User = User(
            id = 1,
            email = "yourssu@gmail.com",
            password = passwordEncoder.encode("asdj"),
            username = "beomsu son"
        )

        private val article: Article = Article(
            id = 1,
            content = "content",
            title = "title",
            user = user
        )

        private lateinit var existenceChecker: ExistenceChecker

        private lateinit var articleService: ArticleService

        const val WRITE = "${RequestURI.ARTICLE}/write"
        const val EDIT = "${RequestURI.ARTICLE}/edit"
        const val DELETE = "${RequestURI.ARTICLE}/delete"


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

            articleService = ArticleService(
                articleRepository = articleRepository,
                userRepository = userRepository
            )

            userRepository.save(user)
            articleRepository.save(article)
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

        val article = Article(
            id = 1,
            content = "content",
            title = "title",
            user = user
        )

        userRepository.save(user)
        articleRepository.save(article)
    }

    @Nested
    @DisplayName("write 메서드 사용할 때")
    inner class DescribeWrite {

        @Nested
        @DisplayName("유저 정보가 일치하지 않을 때가 있을 수 있다.")
        inner class ContextUserIsNotEquals {

            @Nested
            @DisplayName("만약 존재하지 않는 이메일을 기재했다면")
            inner class ContextEmailIsNotEquals {
                @Test
                @DisplayName("CustomException이 발생된다.")
                fun it_throw_CustomException() {

                    // given
                    val requestDto = ArticleRequestDto(
                        title = "title",
                        content = "content"
                    )

                    // when-then
                    assertThrows(CustomException::class.java) {
                        val write = articleService.write(
                            requestDto = requestDto,
                            email = "beomsu@urssu.kr"
                        )

                        println(write)
                    }
                }
            }

        }

        @Nested
        @DisplayName("유저 정보가 일치하면")
        inner class ContextUserEquals {

            private val expectedArticle = Article(
                content = "content",
                title = "title",
                user = user
            )

            private val expectedResult = ArticleResponseDto(
                article = expectedArticle,
                email = user.email
            )

            @Test
            @DisplayName("ArticleResponseDto를 성공적으로 만들어서 준다.")
            fun it_return_articleResponseDto() {

                // given
                val requestDto = ArticleRequestDto(
                    title = "title",
                    content = "content"
                )

                // when
                val result = articleService.write(
                    requestDto = requestDto,
                    email = "yourssu@gmail.com"
                )

                // then
                assertEquals(expectedResult, result)
            }
        }
    }

    @Nested
    @DisplayName("edit 메서드 사용할 때")
    inner class DescribeEdit {

        @Nested
        @DisplayName("유저 정보가 일치하지 않을 때가 있을 수 있다.")
        inner class ContextUserIsNotEquals {

            @Nested
            @DisplayName("만약 존재하지 않는 이메일을 기재했다면")
            inner class ContextEmailIsNotEquals {

                @Test
                @DisplayName("CustomException이 발생된다")
                fun it_throw_CustomException() {

                    // given
                    val requestDto = ArticleRequestDto(
                        title = "title",
                        content = "content"
                    )

                    // when-then
                    assertThrows(CustomException::class.java) {
                        articleService.edit(
                            articleId = 1,
                            requestDto = requestDto,
                            email = "beomsu@urssu.kr"
                        )
                    }
                }
            }

        }

        @Nested
        @DisplayName("유저 정보가 일치하면")
        inner class ContextUserEquals {

            private val expectedArticle = Article(
                id = 1,
                content = "content",
                title = "title",
                user = user
            )

            private val expectedResult = ArticleResponseDto(
                article = expectedArticle,
                email = user.email
            )

            @Nested
            @DisplayName("게시글이 유저의 소유물인지 확인을 한다.")
            inner class ContextCheckOwnership {

                @Nested
                @DisplayName("게시글이 유저의 소유가 아니라면,")
                inner class ContextIncorrectOwnership {

                    private val userForOwnershipCheck = User(
                        id = 2,
                        email = "beomsu@gmail.com",
                        password = "asdf",
                        username = "beomsu"
                    )

                    private val articleForOwnershipCheck = Article(
                        id = 1,
                        content = "content",
                        title = "title",
                        user = userForOwnershipCheck
                    )

                    @Test
                    @DisplayName("CustomException이 발생된다")
                    fun it_throw_customException() {

                        articleRepository.save(articleForOwnershipCheck)

                        // given
                        val requestDto = ArticleRequestDto(
                            title = "title",
                            content = "content"
                        )

                        // when-then
                        assertThrows(CustomException::class.java) {
                            articleService.edit(
                                articleId = 1,
                                requestDto = requestDto,
                                email = "yourssu@gmail.com"
                            )
                        }
                    }
                }

                @Nested
                @DisplayName("게시글이 유저의 소유가 맞다면,")
                inner class ContextCorrectOwnership {

                    @Test
                    @DisplayName("ArticleResponseDto를 성공적으로 만들어서 준다.")
                    fun it_return_articleResponseDto() {

                        // given
                        val requestDto = ArticleRequestDto(
                            title = "title",
                            content = "content"
                        )

                        // when
                        val result = articleService.edit(
                            articleId = 1,
                            requestDto = requestDto,
                            email = "yourssu@gmail.com"
                        )

                        // then
                        assertEquals(expectedResult, result)
                    }
                }
            }
        }
    }

    @Nested
    @DisplayName("delete 메서드를 사용할 때")
    inner class DescribeDelete {

        @Nested
        @DisplayName("유저 정보가 일치하지 않을 때가 있을 수 있다.")
        inner class ContextUserIsNotEquals {

            @Nested
            @DisplayName("만약 존재하지 않는 이메일을 기재했다면")
            inner class ContextEmailIsNotEquals {

                @Test
                @DisplayName("CustomException이 발생한다")
                fun it_throw_CustomException() {

                    // given


                    // when-then
                    assertThrows(CustomException::class.java) {
                        articleService.delete(
                            articleId = 1,
                            currentURI = DELETE,
                            email = "beomsu@urssu.kr"
                        )
                    }
                }
            }

            @Nested
            @DisplayName("만약 이메일은 존재했지만, 비밀번호를 잘못 기재했다면")
            inner class ContextPasswordIsNotEquals {

                @Test
                @DisplayName("CustomException이 발생한다")
                fun it_throw_CustomException() {

                    // given

                    // when-then
                    assertThrows(CustomException::class.java) {
                        articleService.delete(
                            articleId = 1,
                            currentURI = DELETE,
                            email = "beomsu@urssu.kr"
                        )
                    }
                }
            }
        }

        @Nested
        @DisplayName("유저 정보가 일치하면")
        inner class ContextUserEquals {

            @Nested
            @DisplayName("게시글이 유저의 소유물인지 확인을 한다.")
            inner class ContextCheckOwnership {

                @Nested
                @DisplayName("게시글이 유저의 소유가 아니라면,")
                inner class ContextIncorrectOwnership {

                    private val userForOwnershipCheck = User(
                        id = 2,
                        email = "beomsu@gmail.com",
                        password = "asdf",
                        username = "beomsu"
                    )

                    private val articleForOwnershipCheck = Article(
                        id = 1,
                        content = "content",
                        title = "title",
                        user = userForOwnershipCheck
                    )

                    @Test
                    @DisplayName("CustomException이 발생된다")
                    fun it_throw_customException() {

                        articleRepository.save(articleForOwnershipCheck)

                        // given

                        // when-then
                        assertThrows(CustomException::class.java) {
                            articleService.delete(
                                articleId = 1,
                                currentURI = EDIT,
                                email = "beomsu@urssu.kr"
                            )
                        }
                    }
                }

                @Nested
                @DisplayName("게시글이 유저의 소유가 맞다면,")
                inner class ContextCorrectOwnership {

                    @Test
                    @DisplayName("어떠한 exception 없이 성공적으로 함수가 실행된다.")
                    fun it_works_well() {

                        // given

                        // when-then
                        articleService.delete(
                            articleId = 1,
                            currentURI = DELETE,
                            email = "yourssu@gmail.com"
                        )
                    }
                }
            }
        }
    }
}