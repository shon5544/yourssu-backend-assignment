package com.yourssu.assignmentblog.domain.user.domain

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
    val updatedAt: LocalDateTime,

    val email: String,
    val password: String,
    val username: String,

    // 연관관계 매핑은 나중에
)