package com.yourssu.assignmentblog.domain.comment.domain

import com.yourssu.assignmentblog.domain.article.domain.Article
import com.yourssu.assignmentblog.domain.common.BaseCreateAndUpdateTimeEntity
import com.yourssu.assignmentblog.domain.user.domain.User
import javax.persistence.*

@Entity
class Comment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "comment_id")
    val id: Long? = null,

    var content: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    val user: User? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "article_id")
    val article: Article? = null
) : BaseCreateAndUpdateTimeEntity()