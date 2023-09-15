package com.yourssu.assignmentblog.domain.comment.repository.impl

import com.yourssu.assignmentblog.domain.comment.domain.Comment
import com.yourssu.assignmentblog.domain.comment.repository.CommentRepository
import com.yourssu.assignmentblog.domain.comment.repository.jpa.CommentJpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class CommentRepositoryImpl(
    private val commentJpaRepository: CommentJpaRepository
): CommentRepository {
    override fun findById(id: Long): Comment? {
        return commentJpaRepository.findByIdOrNull(id)
    }

    override fun save(comment: Comment): Comment {
        return commentJpaRepository.save(comment)
    }
}