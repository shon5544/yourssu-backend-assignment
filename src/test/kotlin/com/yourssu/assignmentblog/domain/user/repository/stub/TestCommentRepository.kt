package com.yourssu.assignmentblog.domain.user.repository.stub

import com.yourssu.assignmentblog.domain.comment.domain.Comment
import com.yourssu.assignmentblog.domain.comment.repository.CommentRepository

class TestCommentRepository: CommentRepository {
    override fun findById(id: Long): Comment? {
        TODO("Not yet implemented")
    }

    override fun save(comment: Comment): Comment {
        TODO("Not yet implemented")
    }

    override fun delete(comment: Comment) {
        TODO("Not yet implemented")
    }
}