package com.yourssu.assignmentblog.domain.user.dto.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class SignupRequestDto(
    @field:Email
    val email: String = "",

    @field:NotBlank
    var password: String = "",

    @field:NotBlank
    val username: String = ""
)