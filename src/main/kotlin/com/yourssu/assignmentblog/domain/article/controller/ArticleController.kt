package com.yourssu.assignmentblog.domain.article.controller

import com.yourssu.assignmentblog.domain.article.dto.request.ArticleRequestDto
import com.yourssu.assignmentblog.domain.article.dto.response.ArticleResponseDto
import com.yourssu.assignmentblog.domain.article.service.ArticleService
import com.yourssu.assignmentblog.global.auth.jwt.AuthInfo
import com.yourssu.assignmentblog.global.common.uri.RequestURI
import com.yourssu.assignmentblog.global.util.annotation.Auth
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
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
        @RequestBody @Valid requestDto: ArticleRequestDto,
        @Auth authInfo: AuthInfo
    ): ArticleResponseDto {
        return articleService.write(
            requestDto = requestDto,
            currentURI = RequestURI.ARTICLE + "/write",
            email = authInfo.email
        )
    }

    @PutMapping("/edit/{articleId}")
    fun edit(
        @RequestBody @Valid articleRequestDto: ArticleRequestDto,
        @PathVariable articleId: Long,
        @Auth authInfo: AuthInfo
    ): ArticleResponseDto {
        return articleService.edit(
            articleId = articleId,
            currentURI = RequestURI.ARTICLE + "/edit/$articleId",
            requestDto = articleRequestDto,
            email = authInfo.email
        )
    }

    @DeleteMapping("/delete/{articleId}")
    fun delete(
        //@RequestBody @Valid deleteRequestDto: DeleteRequestDto,
        @PathVariable articleId: Long,
        @Auth authInfo: AuthInfo
    ): ResponseEntity<Void> {
        articleService.delete(
            articleId = articleId,
            currentURI = RequestURI.ARTICLE + "/delete/$articleId",
            email = authInfo.email
        )

        return ResponseEntity.ok().build()
    }
}