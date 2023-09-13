package com.yourssu.assignmentblog.domain.user.service

import com.yourssu.assignmentblog.domain.user.domain.User
import com.yourssu.assignmentblog.domain.user.dto.request.SignupRequestDto
import com.yourssu.assignmentblog.domain.user.repository.UserRepository
import com.yourssu.assignmentblog.global.common.exception.CustomException
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    @Transactional
    fun signup(requestDto: SignupRequestDto, currentURI: String): User {
        requestDto.password = passwordEncoder.encode(requestDto.password)

        if (userRepository.findByEmail(requestDto.email) != null)
            throw CustomException(
                status = HttpStatus.BAD_REQUEST,
                message = "회원가입 실패: 해당 email로 이미 가입이 되어있습니다.",
                requestURI = currentURI
            )

        return User(requestDto)
    }
}