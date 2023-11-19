package com.yourssu.assignmentblog.domain.user.controller

import com.yourssu.assignmentblog.domain.user.dto.request.GetUsersRequestDto
import com.yourssu.assignmentblog.domain.user.dto.request.SignupRequestDto
import com.yourssu.assignmentblog.domain.user.dto.response.GetUsersResponseDto
import com.yourssu.assignmentblog.domain.user.dto.response.SignupResponseDto
import com.yourssu.assignmentblog.domain.user.service.UserService
import com.yourssu.assignmentblog.global.auth.jwt.AuthInfo
import com.yourssu.assignmentblog.global.common.uri.RequestURI
import com.yourssu.assignmentblog.global.util.annotation.Auth
import io.swagger.v3.oas.annotations.Operation
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import javax.validation.Valid

@RestController
@RequestMapping(RequestURI.USER)
class UserController(
    private val userService: UserService
) {

    @PostMapping("/signup")
    @Operation(summary = "회원 가입", description = "회원 가입을 합니다.")
    fun signup(
        @RequestBody @Valid requestDto: SignupRequestDto
    ): SignupResponseDto {
        requestDto.setURIAndFailMessage(
            currentURI = RequestURI.USER + "/signup",
            failedTargetText = ""
        )

        return userService.signup(requestDto)
    }

    @DeleteMapping("/withdraw")
    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴를 합니다.")
    fun withdraw(
//        @RequestBody @Valid withDrawRequestDto: DeleteRequestDto,
        @Auth authInfo: AuthInfo
    ): ResponseEntity<Void> {

        userService.withdraw(
            email = authInfo.email,
            currentURI = RequestURI.USER + "/withdraw"
        )

        return ResponseEntity.ok().build()
    }

    @GetMapping("/users")
    @Operation(summary = "유저 정보 검색", description = "ADMIN용 API 입니다. 조건에 맞는 유저를 검색해 정보를 가져옵니다.")
    fun getUsers(
        @RequestParam("username") username: String?,
        @RequestParam("email") email: String?,
        @RequestParam("createdAtStart") @DateTimeFormat(pattern = "yyyy-MM-dd") createdAtStart: LocalDate?,
        @RequestParam("createdAtEnd") @DateTimeFormat(pattern = "yyyy-MM-dd") createdAtEnd: LocalDate?,
        @Auth authInfo: AuthInfo
    ): GetUsersResponseDto {
        val requestDto = GetUsersRequestDto(username, email, createdAtStart, createdAtEnd)

        return userService.getUsers(requestDto, authInfo)
    }
}