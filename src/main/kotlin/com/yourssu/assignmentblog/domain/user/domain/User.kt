package com.yourssu.assignmentblog.domain.user.domain

import com.yourssu.assignmentblog.global.common.enums.Role
import java.time.LocalDateTime
import javax.persistence.*


@Entity
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    val id: Long?,

    @Column(nullable = false, name = "created_at")
    val createdAt: LocalDateTime,

    @Column(nullable = false, name = "updated_at")
    var updatedAt: LocalDateTime,

    var email: String,
    var password: String,
    var username: String,
    var refreshToken: String,

    var role: Role

    // 연관관계 매핑은 나중에
) {
    fun updateRefreshToken(refreshToken: String) {
        this.refreshToken = refreshToken
    }
}