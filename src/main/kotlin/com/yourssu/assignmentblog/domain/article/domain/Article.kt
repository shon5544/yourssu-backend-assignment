package com.yourssu.assignmentblog.domain.article.domain

import com.yourssu.assignmentblog.domain.common.BaseCreateAndUpdateTimeEntity
import com.yourssu.assignmentblog.domain.user.domain.User
import javax.persistence.*

@Entity
class Article(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "article_id")
    private val id: Long? = null,

    @Column(nullable = false)
    var content: String? = null,

    @Column(nullable = false)
    var title: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    val user: User? = null

) : BaseCreateAndUpdateTimeEntity()