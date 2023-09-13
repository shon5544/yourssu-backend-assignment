package com.yourssu.assignmentblog.global.auth.service

import com.yourssu.assignmentblog.domain.user.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException

@Service
class CustomUserDetailsService(private val userRepository: UserRepository): UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails {
        val user = userRepository.findByEmail(email)

        return if (user != null) {
            CustomUserDetails(user)
        } else {
            throw EntityNotFoundException("인증 객체 생성 실패: 해당 email을 가진 유저가 없습니다.")
        }
    }
}