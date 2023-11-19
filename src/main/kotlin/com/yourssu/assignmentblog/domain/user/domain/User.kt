package com.yourssu.assignmentblog.domain.user.domain

import com.yourssu.assignmentblog.domain.article.domain.Article
import com.yourssu.assignmentblog.domain.comment.domain.Comment
import com.yourssu.assignmentblog.global.common.entity.BaseCreateAndUpdateTimeEntity
import com.yourssu.assignmentblog.domain.user.dto.request.SignupRequestDto
import com.yourssu.assignmentblog.domain.user.dto.response.UserVO
import com.yourssu.assignmentblog.global.common.enums.Role
import javax.persistence.*


@Entity
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "user_id")
    private val id: Long? = null,

    var email: String = "",
    var password: String = "",
    var username: String = "",
    var refreshToken: String = "",

    @Enumerated(EnumType.STRING)
    var role: Role? = null,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.REMOVE])
    val commentList: List<Comment> = ArrayList(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.REMOVE])
    val articleList: List<Article> = ArrayList()
) : BaseCreateAndUpdateTimeEntity() {

    constructor(signupRequestDto: SignupRequestDto) : this() {
        this.email = signupRequestDto.email
        this.password = signupRequestDto.password
        this.username = signupRequestDto.username
        this.role = Role.valueOf(signupRequestDto.role)
    }

    fun toUserVO(): UserVO {
        return UserVO(
            id = this.id!!,
            email = this.email,
            username = this.username,
            role = this.role.toString(),
            createdAt = this.createdAt!!,
            updatedAt = this.updatedAt!!
        )
    }
}