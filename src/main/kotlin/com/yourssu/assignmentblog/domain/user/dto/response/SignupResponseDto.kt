package com.yourssu.assignmentblog.domain.user.dto.response

import com.yourssu.assignmentblog.domain.user.domain.User

data class SignupResponseDto(
    var email: String = "",
    var username: String = "",
) {
    constructor(user: User) : this() {
        this.email = user.email
        this.username = user.username
    }
}
