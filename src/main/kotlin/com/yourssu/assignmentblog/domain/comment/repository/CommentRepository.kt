package com.yourssu.assignmentblog.domain.comment.repository

import com.yourssu.assignmentblog.domain.comment.domain.Comment

interface CommentRepository {
    fun findById(id: Long): Comment?

    fun save(comment: Comment): Comment
    fun delete(comment: Comment)
}