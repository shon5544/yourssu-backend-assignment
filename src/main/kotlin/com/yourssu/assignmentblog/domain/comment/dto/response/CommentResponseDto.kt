package com.yourssu.assignmentblog.domain.comment.dto.response

import com.yourssu.assignmentblog.domain.comment.domain.Comment

data class CommentResponseDto(
    var commentId: Long? = null,
    var email: String = "",
    var content: String = "",
) {
    constructor(comment: Comment, email: String) : this() {
        this.commentId = comment.id
        this.email = email
        this.content = comment.content
    }
}
