package com.yourssu.assignmentblog.domain.user.service

import com.yourssu.assignmentblog.domain.user.domain.User
import com.yourssu.assignmentblog.domain.user.dto.request.SignupRequestDto
import com.yourssu.assignmentblog.domain.user.dto.response.SignupResponseDto
import com.yourssu.assignmentblog.domain.user.repository.UserRepository
import com.yourssu.assignmentblog.global.common.aop.ExistenceCheckAspect
import com.yourssu.assignmentblog.global.common.enums.FailedMethod
import com.yourssu.assignmentblog.global.common.enums.FailedTargetType
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
) {

    @Transactional
    fun signup(
        requestDto: SignupRequestDto
    ): SignupResponseDto = ExistenceCheckAspect.checkUserEmailNotExist(
        email = requestDto.email, currentURI = requestDto.currentURI
    ) {
        requestDto.password = passwordEncoder.encode(requestDto.password)

        val user = User(requestDto)

        return@checkUserEmailNotExist SignupResponseDto(userRepository.save(user))
    }

    @Transactional
    fun withdraw(
        email: String,
        currentURI: String,
        failedTargetText: String = "${FailedTargetType.USER} ${FailedMethod.WITHDRAW}"
    ) = ExistenceCheckAspect.checkUserAccount(
        currentURI = currentURI,
        email = email,
        failedTargetText = failedTargetText
    ) {
        val user: User = userRepository.findByEmail(email)!!
        userRepository.delete(user)
    }
}