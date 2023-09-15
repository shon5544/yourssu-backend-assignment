package com.yourssu.assignmentblog.domain.comment.repository.jpa

import com.yourssu.assignmentblog.domain.comment.domain.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentJpaRepository: JpaRepository<Comment, Long> {
}