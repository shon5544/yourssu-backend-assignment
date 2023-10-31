package com.yourssu.assignmentblog.global.common.dto

import io.swagger.v3.oas.annotations.Hidden

abstract class BaseRequestDto(
    @Hidden
    var currentURI: String = "",

    @Hidden
    var failedTargetText: String = ""
) {
    fun setURIAndFailMessage(
        currentURI: String,
        failedTargetText: String
    ) {
        this.currentURI = currentURI
        this.failedTargetText = failedTargetText
    }
}