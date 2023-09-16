package com.yourssu.assignmentblog.domain.user.dto.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class WithdrawRequestDto(
    @field: Email
    val email: String = "",

    @field: NotBlank
    val password: String = ""
)