package com.yourssu.assignmentblog.global.common.repository

import com.yourssu.assignmentblog.global.common.entity.EntityWithOwnership

interface Repository {
    fun findById(id: Long): EntityWithOwnership?
}