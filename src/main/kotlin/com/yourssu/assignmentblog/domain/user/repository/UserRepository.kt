package com.yourssu.assignmentblog.domain.user.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.yourssu.assignmentblog.domain.user.domain.QUser
import com.yourssu.assignmentblog.domain.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

interface UserJpaRepository: JpaRepository<User, Long?> {
    fun findByEmail(email: String): User?
    fun findByRefreshToken(refreshToken: String): User?
}


interface UserRepository {
    fun findByEmail(email: String): User?
    fun findByRefreshToken(refreshToken: String): User?
    fun save(user: User): User
    fun delete(user: User)

    fun findByUsername(username: String): List<User>?
}
@Repository
class UserRepositoryImpl(
    private val userJpaRepository: UserJpaRepository,
    private val jpaQueryFactory: JPAQueryFactory
): UserRepository {

    override fun findByEmail(email: String): User? {
        return userJpaRepository.findByEmail(email)
    }

    override fun findByRefreshToken(refreshToken: String): User? {
        return userJpaRepository.findByRefreshToken(refreshToken)
    }

    override fun save(user: User): User {
        return userJpaRepository.save(user)
    }

    override fun delete(user: User) {
        userJpaRepository.delete(user)
    }

    override fun findByUsername(username: String): List<User>? {
        val qUser = QUser.user
        return jpaQueryFactory.selectFrom(qUser)
            .where(qUser.username.eq(username))
            .orderBy(qUser.id.desc())
            .fetch()
    }

}