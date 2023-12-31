package com.yourssu.assignmentblog.domain.comment.domain

import com.yourssu.assignmentblog.domain.article.domain.Article
import com.yourssu.assignmentblog.domain.comment.dto.request.CommentRequestDto
import com.yourssu.assignmentblog.domain.user.domain.User
import com.yourssu.assignmentblog.global.common.aop.OwnershipCheckAdvice
import com.yourssu.assignmentblog.global.common.entity.BaseCreateAndUpdateTimeEntity
import com.yourssu.assignmentblog.global.common.entity.EntityWithOwnership
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
class Comment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "comment_id")
    val id: Long? = null,
    var content: String = "",
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    override val user: User? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "article_id")
    val article: Article? = null,
) : BaseCreateAndUpdateTimeEntity(), EntityWithOwnership {
    fun update(
        requestDto: CommentRequestDto,
        user: User,
    ) = OwnershipCheckAdvice.checkOwnership(
        target = this,
        currentURI = requestDto.currentURI,
        user = user,
        failedTargetText = requestDto.failedTargetText,
    ) {
        this.content = requestDto.content
    }
}
