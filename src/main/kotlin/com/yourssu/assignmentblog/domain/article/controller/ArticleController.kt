package com.yourssu.assignmentblog.domain.article.controller

import com.yourssu.assignmentblog.domain.article.dto.request.ArticleWriteRequestDto
import com.yourssu.assignmentblog.domain.article.dto.response.ArticleWriteResponseDto
import com.yourssu.assignmentblog.domain.article.service.ArticleService
//import com.yourssu.assignmentblog.global.auth.service.CustomUserDetails
import com.yourssu.assignmentblog.global.common.uri.RequestURI
//import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping(RequestURI.ARTICLE)
class ArticleController(
    private val articleService: ArticleService
) {

    @PostMapping("/write")
    fun write(
        @RequestBody @Valid requestDto: ArticleWriteRequestDto

        // 현재 명세에서는 jwt가 필요없겠다
        // @AuthenticationPrincipal customUserDetails: CustomUserDetails
    ): ArticleWriteResponseDto {
        return articleService.write(requestDto, RequestURI.ARTICLE + "/write")
    }
}