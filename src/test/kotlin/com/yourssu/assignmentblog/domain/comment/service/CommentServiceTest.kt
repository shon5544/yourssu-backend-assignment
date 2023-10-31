package com.yourssu.assignmentblog.domain.comment.service

import com.yourssu.assignmentblog.domain.article.domain.Article
import com.yourssu.assignmentblog.domain.article.repository.ArticleRepository
import com.yourssu.assignmentblog.domain.article.service.ArticleServiceTest
import com.yourssu.assignmentblog.domain.comment.domain.Comment
import com.yourssu.assignmentblog.domain.comment.dto.request.CommentRequestDto
import com.yourssu.assignmentblog.domain.comment.dto.response.CommentResponseDto
import com.yourssu.assignmentblog.domain.comment.repository.CommentRepository
import com.yourssu.assignmentblog.domain.user.domain.User
import com.yourssu.assignmentblog.domain.user.repository.UserRepository
import com.yourssu.assignmentblog.global.common.domain.ExistenceChecker
import com.yourssu.assignmentblog.global.common.domain.OwnershipChecker
import com.yourssu.assignmentblog.global.common.stub.TestArticleRepository
import com.yourssu.assignmentblog.global.common.stub.TestCommentRepository
import com.yourssu.assignmentblog.global.common.stub.TestUserRepository
import com.yourssu.assignmentblog.global.common.uri.RequestURI
import com.yourssu.assignmentblog.global.error.exception.CustomException
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@DisplayName("CommentService 테스트")
internal class CommentServiceTest {

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

        lateinit var commentService: CommentService

        const val WRITE = "${RequestURI.COMMENT}/write"
        const val EDIT = "${RequestURI.COMMENT}/edit"
        const val DELETE = "${RequestURI.COMMENT}/delete"


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

            commentService = CommentService(
                commentRepository = commentRepository,
                existenceChecker = existenceChecker,
                ownershipChecker = ownershipChecker
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

        val comment = Comment(
            id = 1,
            content = "content",
            user = user,
            article = article
        )

        userRepository.save(user)
        articleRepository.save(article)
        commentRepository.save(comment)

        existenceChecker = ExistenceChecker(
            userRepository = userRepository,
            articleRepository = articleRepository,
            commentRepository = commentRepository
        )
        commentService = CommentService(
            commentRepository = commentRepository,
            existenceChecker = existenceChecker,
            ownershipChecker = ownershipChecker
        )

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
                    val email = "beomsu@urssu.kr"

                    val requestDto = CommentRequestDto(
                        content = "content"
                    )

                    // when-then
                    assertThrows(CustomException::class.java) {
                        commentService.write(
                            articleId = 1,
                            requestDto = requestDto,
                            currentURI = ArticleServiceTest.WRITE,
                            email = email
                        )
                    }
                }
            }

        }

        @Nested
        @DisplayName("유저 정보가 일치하면")
        inner class ContextUserEquals {

            private val expectedComment = Comment(
                content = "content",
                user = user,
                article = article
            )

            private val expectedResult = CommentResponseDto(
                email = user.email,
                content = expectedComment.content
            )

            @Nested
            @DisplayName("댓글을 쓰려고 하는 게시물이 존재하는 지 확인한다.")
            inner class ContextCheckArticleExist {

                @Nested
                @DisplayName("만약 게시물이 존재하지 않는다면")
                inner class ContextNotExist {

                    @Test
                    @DisplayName("CustomException이 발생된다")
                    fun it_throw_customException() {

                        val articleRepositoryForNotExistTest = TestArticleRepository()
                        existenceChecker = ExistenceChecker(
                            userRepository = userRepository,
                            articleRepository = articleRepositoryForNotExistTest,
                            commentRepository = commentRepository
                        )
                        commentService = CommentService(
                            commentRepository = commentRepository,
                            existenceChecker = existenceChecker,
                            ownershipChecker = ownershipChecker
                        )

                        // given
                        val email = "yourssu@gmail.com"
                        val requestDto = CommentRequestDto(
                            content = "content"
                        )

                        // when-then
                        assertThrows(CustomException::class.java) {
                            commentService.write(
                                articleId = 1,
                                requestDto = requestDto,
                                currentURI = WRITE,
                                email = email
                            )
                        }
                    }
                }

                @Nested
                @DisplayName("만약 게시물이 잘 존재한다면")
                inner class ContextExist {

                    @Test
                    @DisplayName("CommentResponseDto를 성공적으로 만들어서 준다.")
                    fun it_return_commentResponseDto() {

                        // given
                        val email = "yourssu@gmail.com"
                        val requestDto = CommentRequestDto(
                            content = "content"
                        )

                        // when
                        val result = commentService.write(
                            articleId = 1,
                            requestDto = requestDto,
                            currentURI = WRITE,
                            email = email
                        )

                        // then
                        assertEquals(expectedResult, result)
                    }
                }
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
                @DisplayName("CustomException이 발생된다.")
                fun it_throw_CustomException() {

                    // given
                    val email = "beomsu@urssu.kr"
                    val requestDto = CommentRequestDto(
                        content = "content"
                    )

                    // when-then
                    assertThrows(CustomException::class.java) {
                        commentService.edit(
                            articleId = 1,
                            commentId = 1,
                            requestDto = requestDto,
                            currentURI = ArticleServiceTest.EDIT,
                            email = email
                        )
                    }
                }
            }

        }

        @Nested
        @DisplayName("유저 정보가 일치하면")
        inner class ContextUserEquals {

            private val expectedComment = Comment(
                content = "content",
                user = user,
                article = article
            )

            private val expectedResult = CommentResponseDto(
                email = user.email,
                content = expectedComment.content
            )

            @Nested
            @DisplayName("댓글을 쓰려고 하는 게시물이 존재하는 지 확인한다.")
            inner class ContextCheckArticleExist {

                @Nested
                @DisplayName("만약 게시물이 존재하지 않는다면")
                inner class ContextNotExist {

                    @Test
                    @DisplayName("CustomException이 발생된다")
                    fun it_throw_customException() {

                        val articleRepositoryForNotExistTest = TestArticleRepository()
                        existenceChecker = ExistenceChecker(
                            userRepository = userRepository,
                            articleRepository = articleRepositoryForNotExistTest,
                            commentRepository = commentRepository
                        )
                        commentService = CommentService(
                            commentRepository = commentRepository,
                            existenceChecker = existenceChecker,
                            ownershipChecker = ownershipChecker
                        )

                        // given
                        val email = "yourssu@gmail.com"

                        val requestDto = CommentRequestDto(
                            content = "content"
                        )

                        // when-then
                        assertThrows(CustomException::class.java) {
                            commentService.edit(
                                articleId = 1,
                                commentId = 1,
                                requestDto = requestDto,
                                currentURI = EDIT,
                                email = email
                            )
                        }
                    }
                }

                @Nested
                @DisplayName("만약 게시물이 잘 존재한다면")
                inner class ContextExist {

                    @Nested
                    @DisplayName("댓글이 존재하는 댓글인지 확인한다.")
                    inner class ContextCheckCommentExist {

                        @Nested
                        @DisplayName("만약 존재하지 않는 댓글이라면")
                        inner class ContextNotExist {

                            @Test
                            @DisplayName("CustomException이 발생된다")
                            fun it_throw_customException() {

                                val commentRepositoryForExistTest = TestCommentRepository()
                                existenceChecker = ExistenceChecker(
                                    userRepository = userRepository,
                                    articleRepository = articleRepository,
                                    commentRepository = commentRepositoryForExistTest
                                )
                                commentService = CommentService(
                                    commentRepository = commentRepository,
                                    existenceChecker = existenceChecker,
                                    ownershipChecker = ownershipChecker
                                )

                                // given
                                val email = "yourssu@gmail.com"

                                val requestDto = CommentRequestDto(
                                    content = "content"
                                )

                                // when-then
                                assertThrows(CustomException::class.java) {
                                    commentService.edit(
                                        articleId = 1,
                                        commentId = 1,
                                        requestDto = requestDto,
                                        currentURI = EDIT,
                                        email = email
                                    )
                                }
                            }
                        }

                        @Nested
                        @DisplayName("만약 존재하는 댓글이라면")
                        inner class ContextExist {

                            @Nested
                            @DisplayName("댓글에 대한 소유권 테스트를 한다.")
                            inner class ContextCheckOwnership {

                                private val userForOwnershipCheck = User(
                                    id = 2,
                                    email = "beomsu@gmail.com",
                                    password = "asdf",
                                    username = "beomsu"
                                )

                                private val commentForOwnershipCheck = Comment(
                                    id = 1,
                                    content = "content",
                                    user = userForOwnershipCheck
                                )

                                @Nested
                                @DisplayName("댓글이 유저의 소유가 아니라면")
                                inner class ContextIncorrectOwnership {

                                    @Test
                                    @DisplayName("CustomException이 발생된다.")
                                    fun it_throw_customException() {

                                        commentRepository.save(commentForOwnershipCheck)

                                        // given
                                        val email = "yourssu@gmail.com"

                                        val requestDto = CommentRequestDto(
                                            content = "content"
                                        )

                                        // when-then
                                        assertThrows(CustomException::class.java) {
                                            commentService.edit(
                                                articleId = 1,
                                                commentId = 1,
                                                requestDto = requestDto,
                                                currentURI = EDIT,
                                                email = email
                                            )
                                        }
                                    }
                                }

                                @Nested
                                @DisplayName("댓글이 유저의 소유가 맞다면")
                                inner class ContextCorrectOwnership {

                                    @Test
                                    @DisplayName("CommentResponseDto를 성공적으로 만들어서 준다.")
                                    fun it_return_commentResponseDto() {

                                        // given
                                        val email = "yourssu@gmail.com"

                                        val requestDto = CommentRequestDto(
                                            content = "content"
                                        )

                                        // when
                                        val result = commentService.write(
                                            articleId = 1,
                                            requestDto = requestDto,
                                            currentURI = EDIT,
                                            email = email
                                        )

                                        // then
                                        assertEquals(expectedResult, result)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Nested
    @DisplayName("delete 메서드 사용할 때")
    inner class DescribeDelete {

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
                    val email = "beomsu@urssu.kr"

                    // when-then
                    assertThrows(CustomException::class.java) {
                        commentService.delete(
                            articleId = 1,
                            commentId = 1,
                            currentURI = ArticleServiceTest.DELETE,
                            email = email
                        )
                    }
                }
            }

        }

        @Nested
        @DisplayName("유저 정보가 일치하면")
        inner class ContextUserEquals {

            @Nested
            @DisplayName("댓글을 쓰려고 하는 게시물이 존재하는 지 확인한다.")
            inner class ContextCheckArticleExist {

                @Nested
                @DisplayName("만약 게시물이 존재하지 않는다면")
                inner class ContextNotExist {

                    @Test
                    @DisplayName("CustomException이 발생된다")
                    fun it_throw_customException() {

                        val articleRepositoryForNotExistTest = TestArticleRepository()
                        existenceChecker = ExistenceChecker(
                            userRepository = userRepository,
                            articleRepository = articleRepositoryForNotExistTest,
                            commentRepository = commentRepository
                        )
                        commentService = CommentService(
                            commentRepository = commentRepository,
                            existenceChecker = existenceChecker,
                            ownershipChecker = ownershipChecker
                        )

                        // given
                        val email = "yourssu@gmail.com"

                        // when-then
                        assertThrows(CustomException::class.java) {
                            commentService.delete(
                                articleId = 1,
                                commentId = 1,
                                currentURI = DELETE,
                                email = email
                            )
                        }
                    }
                }

                @Nested
                @DisplayName("만약 게시물이 잘 존재한다면")
                inner class ContextExist {

                    @Nested
                    @DisplayName("댓글이 존재하는 댓글인지 확인한다.")
                    inner class ContextCheckCommentExist {

                        @Nested
                        @DisplayName("만약 존재하지 않는 댓글이라면")
                        inner class ContextNotExist {

                            @Test
                            @DisplayName("CustomException이 발생된다")
                            fun it_throw_customException() {

                                val commentRepositoryForExistTest = TestCommentRepository()
                                existenceChecker = ExistenceChecker(
                                    userRepository = userRepository,
                                    articleRepository = articleRepository,
                                    commentRepository = commentRepositoryForExistTest
                                )
                                commentService = CommentService(
                                    commentRepository = commentRepository,
                                    existenceChecker = existenceChecker,
                                    ownershipChecker = ownershipChecker
                                )

                                // given
                                val email = "yourssu@gmail.com"

                                // when-then
                                assertThrows(CustomException::class.java) {
                                    commentService.delete(
                                        articleId = 1,
                                        commentId = 1,
                                        currentURI = DELETE,
                                        email = email
                                    )
                                }
                            }
                        }

                        @Nested
                        @DisplayName("만약 존재하는 댓글이라면")
                        inner class ContextExist {

                            @Nested
                            @DisplayName("댓글에 대한 소유권 테스트를 한다.")
                            inner class ContextCheckOwnership {

                                private val userForOwnershipCheck = User(
                                    id = 2,
                                    email = "beomsu@gmail.com",
                                    password = "asdf",
                                    username = "beomsu"
                                )

                                private val commentForOwnershipCheck = Comment(
                                    id = 1,
                                    content = "content",
                                    user = userForOwnershipCheck
                                )

                                @Nested
                                @DisplayName("댓글이 유저의 소유가 아니라면")
                                inner class ContextIncorrectOwnership {

                                    @Test
                                    @DisplayName("CustomException이 발생된다.")
                                    fun it_throw_customException() {

                                        commentRepository.save(commentForOwnershipCheck)

                                        // given
                                        val email = "yourssu@gmail.com"

                                        // when-then
                                        assertThrows(CustomException::class.java) {
                                            commentService.delete(
                                                articleId = 1,
                                                commentId = 1,
                                                currentURI = DELETE,
                                                email = email
                                            )
                                        }
                                    }
                                }

                                @Nested
                                @DisplayName("댓글이 유저의 소유가 맞다면")
                                inner class ContextCorrectOwnership {

                                    @Test
                                    @DisplayName("어떠한 Exception 없이 잘 작동한다.")
                                    fun it_works_well() {

                                        // given
                                        val email = "yourssu@gmail.com"

                                        // when-then
                                        commentService.delete(
                                            articleId = 1,
                                            commentId = 1,
                                            currentURI = DELETE,
                                            email = email
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}