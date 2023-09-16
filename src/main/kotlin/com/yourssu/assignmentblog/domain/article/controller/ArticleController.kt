package com.yourssu.assignmentblog.domain.article.controller

import com.yourssu.assignmentblog.global.common.dto.DeleteRequestDto
import com.yourssu.assignmentblog.domain.article.dto.request.ArticleRequestDto
import com.yourssu.assignmentblog.domain.article.dto.response.ArticleResponseDto
import com.yourssu.assignmentblog.domain.article.service.ArticleService
import com.yourssu.assignmentblog.global.common.uri.RequestURI
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
        @RequestBody @Valid requestDto: ArticleRequestDto

        // 현재 명세에서는 jwt가 필요없겠다
        // @AuthenticationPrincipal customUserDetails: CustomUserDetails
    ): ArticleResponseDto {
        return articleService.write(requestDto, RequestURI.ARTICLE + "/write")
    }

    @PutMapping("/edit/{articleId}")
    fun edit(
        @RequestBody @Valid articleRequestDto: ArticleRequestDto,
        @PathVariable articleId: Long
    ): ArticleResponseDto {
        return articleService.edit(
            articleId = articleId,
            currentURI = RequestURI.ARTICLE + "/edit/$articleId",
            requestDto = articleRequestDto)
    }

    @DeleteMapping("/delete/{articleId}")
    fun delete(
        @RequestBody @Valid deleteRequestDto: DeleteRequestDto,
        @PathVariable articleId: Long
    ): ResponseEntity<Void> {
        articleService.delete(articleId, deleteRequestDto, RequestURI.ARTICLE + "/delete/$articleId")

        return ResponseEntity.ok().build()
    }
}