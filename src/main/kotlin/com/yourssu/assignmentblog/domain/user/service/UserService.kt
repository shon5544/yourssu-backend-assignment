package com.yourssu.assignmentblog.domain.user.service

import com.yourssu.assignmentblog.domain.user.domain.User
import com.yourssu.assignmentblog.domain.user.dto.request.SignupRequestDto
import com.yourssu.assignmentblog.domain.user.dto.request.WithdrawRequestDto
import com.yourssu.assignmentblog.domain.user.dto.response.SignupResponseDto
import com.yourssu.assignmentblog.domain.user.repository.UserRepository
import com.yourssu.assignmentblog.global.common.exception.CustomException
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder
) {

    @Transactional
    fun signup(requestDto: SignupRequestDto, currentURI: String): SignupResponseDto {
        requestDto.password = passwordEncoder.encode(requestDto.password)

        if (userRepository.findByEmail(requestDto.email) != null)
            throw CustomException(
                status = HttpStatus.BAD_REQUEST,
                message = "회원가입 실패: 해당 email로 이미 가입이 되어있습니다.",
                requestURI = currentURI
            )

        val user = User(requestDto)

        return SignupResponseDto(userRepository.save(user))
    }

    @Transactional
    fun withdraw(
        requestDto: WithdrawRequestDto,
        currentURI: String
    ) {
        val user = (userRepository.findByEmail(requestDto.email)
            ?: throw CustomException(
                status = HttpStatus.BAD_REQUEST,
                message = "유저 탈퇴 실패: 해당 email에 해당하는 유저가 없습니다.",
                requestURI = currentURI
            ))

        if (!passwordEncoder.matches(requestDto.password, user.password)) {
            throw CustomException(
                status = HttpStatus.BAD_REQUEST,
                message = "유저 탈퇴 실패: 비밀번호가 일치하지 않습니다.",
                requestURI = currentURI
            )
        }

        userRepository.delete(user)
    }
}