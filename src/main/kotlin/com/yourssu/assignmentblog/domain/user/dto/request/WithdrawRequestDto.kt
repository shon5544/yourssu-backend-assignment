package com.yourssu.assignmentblog.domain.user.dto.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class WithdrawRequestDto(
    @field: Email(message = "Email은 누락되면 안 됩니다.")
    val email: String = "",

    @field: NotBlank(message = "Password는 누락되면 안 됩니다.")
    val password: String = ""
)