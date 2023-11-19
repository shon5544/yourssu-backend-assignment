package com.yourssu.assignmentblog.domain.user.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import com.yourssu.assignmentblog.domain.user.domain.QUser.user as qUser
import com.yourssu.assignmentblog.domain.user.domain.User
import com.yourssu.assignmentblog.domain.user.dto.request.GetUsersRequestDto
import com.yourssu.assignmentblog.global.common.enums.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

interface UserJpaRepository: JpaRepository<User, Long?> {
    fun findByEmail(email: String): User?
    fun findByRefreshToken(refreshToken: String): User?
}


interface UserRepository {
    fun findByEmail(email: String): User?
    fun findByRefreshToken(refreshToken: String): User?
    fun save(user: User): User
    fun delete(user: User)

    fun findUsers(userDto: GetUsersRequestDto): List<User>?
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

    override fun findUsers(userDto: GetUsersRequestDto): List<User>? {
        val username = userDto.username
        val email = userDto.email
        val createdAtStart = userDto.createdAtStart
        val createdAtEnd = userDto.createdAtEnd

        val argsNullity = checkArgsAreAllNull(username, email, createdAtStart, createdAtEnd)

        return if (argsNullity) selectUsersByRoleUser()
            else selectUsersByArgs(username!!, email!!, createdAtStart!!, createdAtEnd!!)
    }

    private fun selectUsersByRoleUser(): List<User>? {
        return jpaQueryFactory.selectFrom(qUser)
            .where(qUser.role.eq(Role.USER))
            .orderBy(qUser.id.desc())
            .fetch()
    }

    private fun selectUsersByArgs(username: String, email: String, createdAtStart: LocalDate, createdAtEnd: LocalDate): List<User>? {
        return jpaQueryFactory.selectFrom(qUser)
            .where(
                eqUsername(username),
                eqEmail(email),
                greaterEqDate(createdAtStart),
                lessEqDate(createdAtEnd)
            )
            .orderBy(qUser.id.desc())
            .fetch()
    }

    private fun checkArgsAreAllNull(
        username: String?,
        email: String?,
        createdAtStart: LocalDate?,
        createdAtEnd: LocalDate?
    ): Boolean {
        return username == null &&
        email == null &&
        createdAtStart == null &&
        createdAtEnd == null
    }

    private fun eqUsername(username: String?): BooleanExpression? {
        return if (username == null) null else qUser.username.eq(username)
    }

    private fun eqEmail(email: String?): BooleanExpression? {
        return if (email == null) null else qUser.email.eq(email)
    }

    private fun greaterEqDate(date: LocalDate?): BooleanExpression? {
        return if (date == null) null else qUser.createdAt.goe(date)
    }

    private fun lessEqDate(date: LocalDate?): BooleanExpression? {
        return if (date == null) null else qUser.createdAt.loe(date)
    }

}