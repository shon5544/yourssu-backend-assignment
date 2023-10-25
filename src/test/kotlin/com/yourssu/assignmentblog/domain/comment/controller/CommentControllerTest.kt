package com.yourssu.assignmentblog.domain.comment.controller

import com.yourssu.assignmentblog.domain.article.domain.Article
import com.yourssu.assignmentblog.domain.article.repository.ArticleRepository
import com.yourssu.assignmentblog.domain.comment.domain.Comment
import com.yourssu.assignmentblog.domain.comment.dto.request.CommentRequestDto
import com.yourssu.assignmentblog.domain.comment.dto.response.CommentResponseDto
import com.yourssu.assignmentblog.domain.comment.repository.CommentRepository
import com.yourssu.assignmentblog.domain.comment.service.CommentService
import com.yourssu.assignmentblog.domain.user.domain.User
import com.yourssu.assignmentblog.domain.user.repository.UserRepository
import com.yourssu.assignmentblog.global.auth.jwt.AuthInfo
import com.yourssu.assignmentblog.global.common.domain.ExistenceChecker
import com.yourssu.assignmentblog.global.common.domain.OwnershipChecker
import com.yourssu.assignmentblog.global.common.stub.TestArticleRepository
import com.yourssu.assignmentblog.global.common.stub.TestCommentRepository
import com.yourssu.assignmentblog.global.common.stub.TestUserRepository
import com.yourssu.assignmentblog.global.common.uri.RequestURI
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@DisplayName("CommentController 테스트")
internal class CommentControllerTest {

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

        private lateinit var commentService: CommentService

        private lateinit var commentController: CommentController

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

            commentController = CommentController(commentService)
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

        commentController = CommentController(commentService)
    }

    @Test
    @DisplayName("write 테스트")
    fun write() {
        // given
        val requestDto = CommentRequestDto(
            content = "content"
        )

        // when
        val result = commentController.write(
            commentRequestDto = requestDto,
            articleId = 1,
            authInfo = AuthInfo("yourssu@gmail.com")
        )


        // then
        val expectedResult = CommentResponseDto(
            email = user.email,
            content = "content"
        )
        assertEquals(expectedResult, result)
    }

    @Test
    @DisplayName("edit 테스트")
    fun edit() {
        // given
        val requestDto = CommentRequestDto(
            content = "content"
        )

        // when
        val result = commentController.edit(
            commentRequestDto = requestDto,
            articleId = 1,
            commentId = 1,
            authInfo = AuthInfo("yourssu@gmail.com")
        )

        // then
        val expectedResult = CommentResponseDto(
            commentId = 1,
            email = user.email,
            content = "content"
        )
        assertEquals(expectedResult, result)
    }

    @Test
    @DisplayName("delete 테스트")
    fun delete() {
        // given

        // when-then
        commentController.delete(
            articleId = 1,
            commentId = 1,
            authInfo = AuthInfo("yourssu@gmail.com")
        )
    }
}