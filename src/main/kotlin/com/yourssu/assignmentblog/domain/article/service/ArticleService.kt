package com.yourssu.assignmentblog.domain.article.service

import com.yourssu.assignmentblog.domain.article.domain.Article
import com.yourssu.assignmentblog.domain.article.dto.request.ArticleWriteRequestDto
import com.yourssu.assignmentblog.domain.article.dto.response.ArticleWriteResponseDto
import com.yourssu.assignmentblog.domain.article.repository.ArticleRepository
import com.yourssu.assignmentblog.domain.user.repository.UserRepository
import com.yourssu.assignmentblog.global.common.exception.CustomException
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class ArticleService(
    private val userRepository: UserRepository,
    private val articleRepository: ArticleRepository,
    private val passwordEncoder: BCryptPasswordEncoder
) {

    fun write(requestDto: ArticleWriteRequestDto, currentURI: String): ArticleWriteResponseDto {
        val user = (userRepository.findByEmail(requestDto.email)
            ?: throw CustomException(
                status = HttpStatus.BAD_REQUEST,
                message = "게시글 작성 실패: 해당 email에 해당하는 유저가 없습니다.",
                requestURI = currentURI
            ))

        if (!passwordEncoder.matches(requestDto.password, user.password)) {
            throw CustomException(
                status = HttpStatus.BAD_REQUEST,
                message = "게시글 작성 실패: 비밀번호가 일치하지 않습니다.",
                requestURI = currentURI
            )
        }

        val article = Article(
            content = requestDto.content,
            title = requestDto.title,
            user = user
        )

        return ArticleWriteResponseDto(
            articleRepository.save(article),
            user.email)
    }

    @Transactional
    fun edit(
        articleId: Long,
        currentURI: String,
        requestDto: ArticleWriteRequestDto
        ): ArticleWriteResponseDto {
        val user = (userRepository.findByEmail(requestDto.email)
            ?: throw CustomException(
                status = HttpStatus.BAD_REQUEST,
                message = "게시글 수정 실패: 해당 email에 해당하는 유저가 없습니다.",
                requestURI = currentURI
            ))

        if (!passwordEncoder.matches(requestDto.password, user.password)) {
            throw CustomException(
                status = HttpStatus.BAD_REQUEST,
                message = "게시글 수정 실패: 비밀번호가 일치하지 않습니다.",
                requestURI = currentURI
            )
        }

        val article = articleRepository.findById(articleId)
            ?: throw CustomException(
                status = HttpStatus.BAD_REQUEST,
                message = "게시글 수정 실패: 해당 Id를 가진 게시글이 없습니다.",
                requestURI = currentURI
            )

        if (!article.user?.equals(user)!!)
            throw throw CustomException(
                status = HttpStatus.BAD_REQUEST,
                message = "게시글 수정 실패: 해당 게시글은 해당 유저의 소유가 아닙니다.",
                requestURI = currentURI
            )

        article.title = requestDto.title
        article.content = requestDto.content

        return ArticleWriteResponseDto(
            article, article.user.email)
    }
}