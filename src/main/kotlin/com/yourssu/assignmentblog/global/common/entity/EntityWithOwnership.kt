package com.yourssu.assignmentblog.global.common.entity

import com.yourssu.assignmentblog.domain.user.domain.User

interface EntityWithOwnership {
    val user: User?
}