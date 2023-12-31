package com.yourssu.assignmentblog.global.auth.jwt

import com.yourssu.assignmentblog.domain.user.domain.User
import com.yourssu.assignmentblog.domain.user.repository.UserRepository
import com.yourssu.assignmentblog.global.auth.jwt.token.TokenProvider
import com.yourssu.assignmentblog.global.common.localDateTImeHolder.CustomLocalDateTime
import com.yourssu.assignmentblog.global.common.stub.StubHttpServletRequest
import com.yourssu.assignmentblog.global.common.stub.StubHttpServletResponse
import com.yourssu.assignmentblog.global.common.stub.TestCustomLocalDateTime
import com.yourssu.assignmentblog.global.common.stub.TestUserRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("JwtTokenManager 테스트")
internal class JwtTokenManagerTest {
    companion object {
        lateinit var tokenProvider: TokenProvider
        private val userRepository: UserRepository = TestUserRepository()

        var expectedAccessToken: String = ""
        var expectedRefreshToken: String = ""

        @BeforeAll
        @JvmStatic
        fun initialize() {
//            tokenExtractor = JwtTokenManager(
//
//                accessTokenExpiration = 24,
//                refreshTokenExpiration = 336,
//                localDateTime = TestCustomLocalDateTime(),
//                userRepository = userRepository
//            )

            tokenProvider =
                TokenProvider(
                    secretKey = "testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttest",
                    accessTokenExpiration = 24,
                    refreshTokenExpiration = 336,
                    localDateTime = TestCustomLocalDateTime(),
                    userRepository = userRepository,
                )

            // 모두 임의의 날짜를 넣어서 만든 임의의 토큰들입니다.
            expectedAccessToken = tokenProvider.createAccessToken(User(email = "yourssu@gmail.com"))
            expectedRefreshToken = tokenProvider.createRefreshToken()
        }
    }

    @Nested
    @DisplayName("createAccessToken 메서드가")
    inner class DescribeCreateAccessToken {
        private val user: User

        init {
            user =
                User(
                    id = 1,
                    email = "yourssu@gmail.com",
                    password = "asdj",
                    username = "beomsu son",
                )
        }

        @Nested
        @DisplayName("정상적으로 매개변수를 받았다면")
        inner class ContextNormalArgs {
            @Test
            @DisplayName("Access Token을 발행해준다")
            fun it_returns_access_token() {
                val accessToken = tokenProvider.createAccessToken(user)

                assertEquals(expectedAccessToken, accessToken)
            }
        }
    }

    @Nested
    @DisplayName("createRefreshToken 메서드는")
    inner class DescribeCreateRefreshToken {
        @Test
        @DisplayName("매개변수 없이 Refresh Token을 발행한다.")
        fun it_returns_refreshToken() {
            val refreshToken = tokenProvider.createRefreshToken()

            assertEquals(expectedRefreshToken, refreshToken)
        }
    }

    @Nested
    @DisplayName("extractToken 메서드는 요청 헤더에서 토큰을 추출해준다.")
    inner class DescribeExtractToken {
        private val localJwtTokenManager =
            JwtTokenManager(
                secretKey = "testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttest",
                accessTokenExpiration = 24,
                refreshTokenExpiration = 336,
                localDateTime = CustomLocalDateTime(),
                userRepository = userRepository,
            )

        @Nested
        @DisplayName("Access Token을 요청 헤더에서 추출할 때")
        inner class ContextExtractAccessTokenInHeader {
            @Nested
            @DisplayName("Bearer가 잘 붙어있으면")
            inner class ContextBearerIsNormallyInAccessToken {
                @Test
                @DisplayName("Access Token이 제대로 추출된다.")
                fun it_returns_access_token() {
                    tokenProvider =
                        TokenProvider(
                            secretKey = "testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttest",
                            accessTokenExpiration = 24,
                            refreshTokenExpiration = 336,
                            localDateTime = CustomLocalDateTime(),
                            userRepository = userRepository,
                        )

                    // 모두 임의의 날짜를 넣어서 만든 임의의 토큰들입니다.
                    expectedAccessToken = tokenProvider.createAccessToken(User(email = "yourssu@gmail.com"))
                    expectedRefreshToken = tokenProvider.createRefreshToken()

                    val extractedToken =
                        localJwtTokenManager.extractToken(
                            "Authorization",
                            StubHttpServletRequest(expectedAccessToken, expectedRefreshToken),
                            StubHttpServletResponse(),
                        )

                    assertEquals(expectedAccessToken, extractedToken)
                }
            }

            @Nested
            @DisplayName("Bearer가 안 붙어있으면")
            inner class ContextBearerIsNotNormallyInAccessToken {
                @Test
                @DisplayName("IllegalArgumentException이 터진다.")
                fun it_throws_IllegalArgumentException() {
                    assertThrows(IllegalArgumentException::class.java) {
                        // 실제 운영코드에선 이런 헤더가 올 수 없음
                        // 지금 테스트 해보려는건 getHeader 메소드가 아닌,
                        // extractToken이 Bearer 여부를 잘 감지하는 가? 에 관한 것이기 때문에
                        // 헤더는 임의의 테스트 전용 헤더로 넣었습니다.
                        localJwtTokenManager.extractToken(
                            "Authorization-no-bearer",
                            StubHttpServletRequest(expectedAccessToken, expectedRefreshToken),
                            StubHttpServletResponse(),
                        )
                    }
                }
            }
        }

        @Nested
        @DisplayName("Refresh Token을 추출할 때")
        inner class ContextExtractRefreshToken {
            @Nested
            @DisplayName("Bearer가 잘 붙어있으면")
            inner class ContextBearerIsNormallyInAccessToken {
                @Test
                @DisplayName("Refresh Token이 제대로 추출된다.")
                fun it_returns_refresh_token() {
                    tokenProvider =
                        TokenProvider(
                            secretKey = "testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttest",
                            accessTokenExpiration = 24,
                            refreshTokenExpiration = 336,
                            localDateTime = CustomLocalDateTime(),
                            userRepository = userRepository,
                        )

                    // 모두 임의의 날짜를 넣어서 만든 임의의 토큰들입니다.
                    expectedAccessToken = tokenProvider.createAccessToken(User(email = "yourssu@gmail.com"))
                    expectedRefreshToken = tokenProvider.createRefreshToken()

                    val extractedToken =
                        localJwtTokenManager.extractToken(
                            "Authorization-refresh",
                            StubHttpServletRequest(expectedAccessToken, expectedRefreshToken),
                            StubHttpServletResponse(),
                        )

                    assertEquals(expectedRefreshToken, extractedToken)
                }
            }

            @Nested
            @DisplayName("Bearer가 안 붙어있으면")
            inner class ContextBearerIsNotNormallyInAccessToken {
                @Test
                @DisplayName("IllegalArgumentException이 터진다.")
                fun it_throws_IllegalArgumentException() {
                    assertThrows(IllegalArgumentException::class.java) {
                        // 실제 운영코드에선 이런 헤더가 올 수 없음
                        // 지금 테스트 해보려는건 getHeader 메소드가 아닌,
                        // extractToken이 Bearer 여부를 잘 감지하는 가? 에 관한 것이기 때문에
                        // 헤더는 임의의 테스트 전용 헤더로 넣었습니다.
                        localJwtTokenManager.extractToken(
                            "Authorization-refresh-no-bearer",
                            StubHttpServletRequest(expectedAccessToken, expectedRefreshToken),
                            StubHttpServletResponse(),
                        )
                    }
                }
            }
        }
    }

    @Nested
    @DisplayName("isTokenValid는 토큰의 유효성을 확인한다.")
    inner class DescribeIsTokenValid {
        private val localJwtTokenManager =
            JwtTokenManager(
                secretKey = "testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttest",
                accessTokenExpiration = 24,
                refreshTokenExpiration = 336,
                localDateTime = CustomLocalDateTime(),
                userRepository = userRepository,
            )

        @Nested
        @DisplayName("유효 기간이 지나지 않은 토큰을 넣어준다면")
        inner class ContextPutNormalToken {
            private val token = localJwtTokenManager.createRefreshToken()
            private val tokenValid = localJwtTokenManager.isTokenValid(token)

            @Test
            @DisplayName("true를 반환한다")
            fun it_returns_true() {
                // 토큰 종류는 상관이 없음.
                // 유효 기간 관련 검증은 access나 refresh나 다를게 없음
                assertTrue(tokenValid)
            }
        }

        @Nested
        @DisplayName("유효 기간이 지난 토큰을 넣어준다면")
        inner class ContextPutExpiredToken {
            private val token =
                "eyJhbGciOiJIUzI1NiJ9." +
                    "eyJzdWIiOiJSZWZyZXNoVG9rZW4iLCJleHAiOjE2OTM2MDY3MDB9." +
                    "cFxYbLRDIHouLkRqY3e5ehqsgYHiS7jV-QwRX4FwWds"
            private val tokenValid = localJwtTokenManager.isTokenValid(token)

            @Test
            @DisplayName("false를 반환한다")
            fun it_returns_true() {
                assertFalse(tokenValid)
            }
        }
    }
}
