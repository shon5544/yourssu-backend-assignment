package com.yourssu.assignmentblog.global.util.annotation

import io.swagger.v3.oas.annotations.media.Schema

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Schema(accessMode = Schema.AccessMode.READ_ONLY)
annotation class Auth