package com.yourssu.assignmentblog.domain.user.service

import com.yourssu.assignmentblog.domain.user.domain.User
import com.yourssu.assignmentblog.domain.user.dto.request.GetUsersRequestDto
import com.yourssu.assignmentblog.domain.user.dto.request.SignupRequestDto
import com.yourssu.assignmentblog.domain.user.dto.response.GetUsersResponseDto
import com.yourssu.assignmentblog.domain.user.dto.response.SignupResponseDto
import com.yourssu.assignmentblog.domain.user.dto.response.UserVO
import com.yourssu.assignmentblog.domain.user.repository.UserRepository
import com.yourssu.assignmentblog.global.auth.jwt.AuthInfo
import com.yourssu.assignmentblog.global.common.aop.ExistenceCheckAdvice
import com.yourssu.assignmentblog.global.common.enums.FailedMethod
import com.yourssu.assignmentblog.global.common.enums.FailedTargetType
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.IllegalArgumentException

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
) {

    @Transactional
    fun signup(
        requestDto: SignupRequestDto
    ): SignupResponseDto = ExistenceCheckAdvice.checkUserEmailNotExist(
        email = requestDto.email,
        currentURI = requestDto.currentURI,
        userRepository = userRepository
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
    ) = ExistenceCheckAdvice.checkUserAccount(
        currentURI = currentURI,
        email = email,
        failedTargetText = failedTargetText,
        userRepository = userRepository
    ) {
        val user: User = userRepository.findByEmail(email)!!
        userRepository.delete(user)
    }

    fun getUsers(requestDto: GetUsersRequestDto, authInfo: AuthInfo): GetUsersResponseDto {
        userRepository.findByEmail(authInfo.email) ?: throw IllegalArgumentException("유저 정보 조회 실패: 유저 정보를 조회할 권한이 없습니다.")

        val users: List<User> = userRepository.findUsers(requestDto)
            ?: throw IllegalArgumentException("유저 정보 조회 실패: 요청 정보가 잘못되었습니다. 쿼리 조건을 다시 한번 확인하십시오.")

        val userVOList: List<UserVO> = users.map { user: User ->
            user.toUserVO()
        }

        return GetUsersResponseDto(userVOList)
    }
}