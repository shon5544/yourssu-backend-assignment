package com.yourssu.assignmentblog.domain.article.controller

import com.yourssu.assignmentblog.domain.article.domain.Article
import com.yourssu.assignmentblog.domain.article.dto.request.ArticleRequestDto
import com.yourssu.assignmentblog.domain.article.dto.response.ArticleResponseDto
import com.yourssu.assignmentblog.domain.article.repository.ArticleRepository
import com.yourssu.assignmentblog.domain.article.service.ArticleService
import com.yourssu.assignmentblog.domain.comment.repository.CommentRepository
import com.yourssu.assignmentblog.domain.user.domain.User
import com.yourssu.assignmentblog.domain.user.repository.UserRepository
import com.yourssu.assignmentblog.global.auth.jwt.AuthInfo
import com.yourssu.assignmentblog.global.common.domain.ExistenceChecker
import com.yourssu.assignmentblog.global.common.domain.OwnershipChecker
import com.yourssu.assignmentblog.global.common.stub.TestArticleRepository
import com.yourssu.assignmentblog.global.common.stub.TestCommentRepository
import com.yourssu.assignmentblog.global.common.stub.TestUserRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@DisplayName("ArticleController 테스트")
internal class ArticleControllerTest {
    // dto 멤버 누락 부분은 테스트하지 않음
    // 복잡한 케이스 또한 Service에서 테스트 중이니 테스트 X
    // 각종 메서드가 잘 작동 되는지만 테스트하기

    companion object {
        private lateinit var userRepository: UserRepository
        private lateinit var articleRepository: ArticleRepository
        private lateinit var commentRepository: CommentRepository

        private val passwordEncoder = BCryptPasswordEncoder()
        private val ownershipChecker = OwnershipChecker()

        private val user: User =
            User(
                id = 1,
                email = "yourssu@gmail.com",
                password = passwordEncoder.encode("asdj"),
                username = "beomsu son",
            )

        private val article: Article =
            Article(
                // 운영코드에서 write를 할 때는 Id 어노테이션 때문에 따로 초기화를 안 한다.
                // 그래서 테스트 할 때는 어쩔 수 없이 null이 나올 수 없기 때문에 id 필드는 생략한다.
                content = "content",
                title = "title",
                user = user,
            )

        private lateinit var existenceChecker: ExistenceChecker

        private lateinit var articleService: ArticleService

        private lateinit var articleController: ArticleController

        @BeforeAll
        @JvmStatic
        fun initialize() {
            userRepository = TestUserRepository()
            articleRepository = TestArticleRepository()
            commentRepository = TestCommentRepository()

            existenceChecker =
                ExistenceChecker(
                    userRepository = userRepository,
                    articleRepository = articleRepository,
                    commentRepository = commentRepository,
                )

            articleService =
                ArticleService(
                    articleRepository = articleRepository,
                    userRepository = userRepository,
                )

            userRepository.save(user)
            articleRepository.save(article)

            articleController = ArticleController(articleService)
        }
    }

    @BeforeEach
    fun initializeEach() {
        val user =
            User(
                id = 1,
                email = "yourssu@gmail.com",
                password = passwordEncoder.encode("asdj"),
                username = "beomsu son",
            )

        val article =
            Article(
                content = "content",
                title = "title",
                user = user,
            )

        userRepository.save(user)
        articleRepository.save(article)
    }

    @Test
    @DisplayName("write 테스트")
    fun write() {
        // given
        val requestDto =
            ArticleRequestDto(
                title = "title",
                content = "content",
            )

        // when
        val result = articleController.write(requestDto, AuthInfo("yourssu@gmail.com"))

        // then
        val expectedResult =
            ArticleResponseDto(
                article = article,
                email = user.email,
            )
        assertEquals(expectedResult, result)
    }

    @Test
    @DisplayName("edit 테스트")
    fun edit() {
        // given
        val requestDto =
            ArticleRequestDto(
                title = "title",
                content = "content",
            )

        // when
        val result =
            articleController.edit(
                requestDto = requestDto,
                articleId = 1,
                authInfo = AuthInfo("yourssu@gmail.com"),
            )

        // then
        val expectedResult =
            ArticleResponseDto(
                article = article,
                email = user.email,
            )
        assertEquals(expectedResult, result)
    }

    @Test
    @DisplayName("delete 테스트")
    fun delete() {
        // given

        // when-then
        articleController.delete(
            articleId = 1,
            authInfo = AuthInfo("yourssu@gmail.com"),
        )
    }
}
