package com.yourssu.assignmentblog.domain.user.controller

import com.yourssu.assignmentblog.domain.user.dto.request.SignupRequestDto
import com.yourssu.assignmentblog.domain.user.dto.response.SignupResponseDto
import com.yourssu.assignmentblog.domain.user.service.UserService
import com.yourssu.assignmentblog.global.auth.jwt.AuthInfo
import com.yourssu.assignmentblog.global.common.uri.RequestURI
import com.yourssu.assignmentblog.global.util.annotation.Auth
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping(RequestURI.USER)
class UserController(
    private val userService: UserService
) {

    @PostMapping("/signup")
    fun signup(
        @RequestBody @Valid signupRequestDto: SignupRequestDto): SignupResponseDto {
        return userService.signup(signupRequestDto, RequestURI.USER + "/signup")
    }

    @DeleteMapping("/withdraw")
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
}