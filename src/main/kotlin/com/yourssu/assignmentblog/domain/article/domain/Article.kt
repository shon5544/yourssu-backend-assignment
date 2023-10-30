package com.yourssu.assignmentblog.domain.article.domain

import com.yourssu.assignmentblog.domain.article.dto.request.ArticleRequestDto
import com.yourssu.assignmentblog.domain.comment.domain.Comment
import com.yourssu.assignmentblog.global.common.entity.BaseCreateAndUpdateTimeEntity
import com.yourssu.assignmentblog.domain.user.domain.User
import com.yourssu.assignmentblog.global.common.aop.OwnershipCheckAdviceHolder
import com.yourssu.assignmentblog.global.common.entity.EntityWithOwnership
import javax.persistence.*

@Entity
class Article(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "article_id")
    val id: Long? = null,

    @Column(nullable = false)
    var content: String = "",

    @Column(nullable = false)
    var title: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    override val user: User? = null,

    @OneToMany(mappedBy = "article", cascade = [CascadeType.REMOVE])
    val commentList: List<Comment> = ArrayList()
) : BaseCreateAndUpdateTimeEntity(), EntityWithOwnership {

    fun update(
        requestDto: ArticleRequestDto,
        user: User
    ) = OwnershipCheckAdviceHolder.checkOwnership(
        target = this,
        currentURI = requestDto.currentURI,
        failedTargetText = requestDto.failedTargetText,
        user = user
    ) {
        this.title = requestDto.title
        this.content = requestDto.content
    }
}