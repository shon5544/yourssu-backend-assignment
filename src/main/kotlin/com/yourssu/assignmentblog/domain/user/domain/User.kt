package com.yourssu.assignmentblog.domain.user.domain

import com.yourssu.assignmentblog.domain.comment.domain.Comment
import com.yourssu.assignmentblog.global.common.enums.Role
import java.time.LocalDateTime
import javax.persistence.*


@Entity
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "user_id")
    val id: Long? = null,

    @Column(nullable = false, name = "created_at")
    val createdAt: LocalDateTime? = null,

    @Column(nullable = false, name = "updated_at")
    var updatedAt: LocalDateTime? = null,

    var email: String = "",
    var password: String = "",
    var username: String = "",
    var refreshToken: String = "",

    var role: Role? = null,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.REMOVE])
    val commentList: List<Comment> = ArrayList()
) {
    fun updateRefreshToken(refreshToken: String) {
        this.refreshToken = refreshToken
    }
}