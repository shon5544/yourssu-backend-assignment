package com.yourssu.assignmentblog.domain.comment.domain

import com.yourssu.assignmentblog.domain.user.domain.User
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Comment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "comment_id")
    val id: Long? = null,

    val createdAt: LocalDateTime? = null,

    var updatedAt: LocalDateTime? = null,
    var content: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User
)