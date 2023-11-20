package com.yourssu.assignmentblog.global.common.stub

import com.yourssu.assignmentblog.domain.comment.domain.Comment
import com.yourssu.assignmentblog.domain.comment.repository.CommentRepository

class TestCommentRepository : CommentRepository {
    private var comment: Comment? = null

    override fun findById(id: Long): Comment? {
        return comment
    }

    override fun save(comment: Comment): Comment {
        this.comment = comment
        return comment
    }

    override fun delete(comment: Comment) {
        return
    }
}
