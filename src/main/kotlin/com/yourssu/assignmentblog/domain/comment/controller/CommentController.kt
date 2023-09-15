package com.yourssu.assignmentblog.domain.comment.controller

import com.yourssu.assignmentblog.domain.comment.dto.request.CommentWriteRequestDto
import com.yourssu.assignmentblog.domain.comment.dto.response.CommentWriteResponseDto
import com.yourssu.assignmentblog.domain.comment.service.CommentService
import com.yourssu.assignmentblog.global.common.uri.RequestURI
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping(RequestURI.COMMENT)
class CommentController(
    private val commentService: CommentService
) {

    @PostMapping("/write/{articleId}")
    fun write(
        @RequestBody @Valid commentWriteRequestDto: CommentWriteRequestDto,
        @PathVariable articleId: Long
    ): CommentWriteResponseDto {
        return commentService.write(
            articleId = articleId,
            currentURI = RequestURI.COMMENT + "/write",
            requestDto = commentWriteRequestDto)
    }

    @PostMapping("/edit")
    fun edit(
        @RequestBody @Valid commentWriteRequestDto: CommentWriteRequestDto,
        @RequestParam(name = "article") articleId: Long,
        @RequestParam(name = "comment") commentId: Long
    ): CommentWriteResponseDto {
        return commentService.edit(
            articleId = articleId,
            commentId = commentId,
            currentURI = RequestURI.COMMENT + "/edit?article=${articleId}&comment=${commentId}",
            requestDto = commentWriteRequestDto
        )
    }
}