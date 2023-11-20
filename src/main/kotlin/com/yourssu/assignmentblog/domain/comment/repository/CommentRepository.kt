package com.yourssu.assignmentblog.domain.comment.repository

import com.yourssu.assignmentblog.domain.comment.domain.Comment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

interface CommentJpaRepository : JpaRepository<Comment, Long>

interface CommentRepository {
    fun findById(id: Long): Comment?

    fun save(comment: Comment): Comment

    fun delete(comment: Comment)
}

@Repository
class CommentRepositoryImpl(
    private val commentJpaRepository: CommentJpaRepository,
) : CommentRepository {
    override fun findById(id: Long): Comment? {
        return commentJpaRepository.findByIdOrNull(id)
    }

    override fun save(comment: Comment): Comment {
        return commentJpaRepository.save(comment)
    }

    override fun delete(comment: Comment) {
        commentJpaRepository.delete(comment)
    }
}
