package com.yourssu.assignmentblog.domain.user.service

import com.yourssu.assignmentblog.domain.user.domain.User
import com.yourssu.assignmentblog.domain.user.dto.request.SignupRequestDto
import com.yourssu.assignmentblog.domain.user.dto.request.WithdrawRequestDto
import com.yourssu.assignmentblog.domain.user.dto.response.SignupResponseDto
import com.yourssu.assignmentblog.domain.user.repository.UserRepository
import com.yourssu.assignmentblog.global.common.domain.ExistenceChecker
import com.yourssu.assignmentblog.global.common.enums.FailedMethod
import com.yourssu.assignmentblog.global.common.enums.FailedTargetType
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val existenceChecker: ExistenceChecker
) {

    @Transactional
    fun signup(requestDto: SignupRequestDto, currentURI: String): SignupResponseDto {
        requestDto.password = passwordEncoder.encode(requestDto.password)

        val failedTargetText = "${FailedTargetType.USER} ${FailedMethod.SIGNUP}"

        existenceChecker.checkUserEmail(
            email = requestDto.email,
            failedTargetText = failedTargetText,
            currentURI = currentURI
        )

        val user = User(requestDto)

        return SignupResponseDto(userRepository.save(user))
    }

    @Transactional
    fun withdraw(
        requestDto: WithdrawRequestDto,
        currentURI: String
    ) {
        val failedTargetText = "${FailedTargetType.USER} ${FailedMethod.WITHDRAW}"

        val user = existenceChecker.checkUserAccount(
            currentURI = currentURI,
            email = requestDto.email,
            password = requestDto.password,
            failedTargetText = failedTargetText
        )

        userRepository.delete(user)
    }
}