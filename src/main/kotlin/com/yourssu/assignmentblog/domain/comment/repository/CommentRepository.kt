package com.yourssu.assignmentblog.domain.comment.repository

import com.yourssu.assignmentblog.domain.comment.domain.Comment

interface CommentRepository {
    fun findByEmail(email: String): Comment?
    fun save(comment: Comment): Comment
}