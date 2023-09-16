package com.yourssu.assignmentblog.domain.comment.controller

import com.yourssu.assignmentblog.global.common.dto.DeleteRequestDto
import com.yourssu.assignmentblog.domain.comment.dto.request.CommentRequestDto
import com.yourssu.assignmentblog.domain.comment.dto.response.CommentWriteResponseDto
import com.yourssu.assignmentblog.domain.comment.service.CommentService
import com.yourssu.assignmentblog.global.common.uri.RequestURI
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
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
        @RequestBody @Valid commentRequestDto: CommentRequestDto,
        @PathVariable articleId: Long
    ): CommentWriteResponseDto {
        return commentService.write(
            articleId = articleId,
            currentURI = RequestURI.COMMENT + "/write",
            requestDto = commentRequestDto)
    }

    @PutMapping("/edit")
    fun edit(
        @RequestBody @Valid commentRequestDto: CommentRequestDto,
        @RequestParam(name = "article") articleId: Long,
        @RequestParam(name = "comment") commentId: Long
    ): CommentWriteResponseDto {
        return commentService.edit(
            articleId = articleId,
            commentId = commentId,
            currentURI = RequestURI.COMMENT + "/edit?article=${articleId}&comment=${commentId}",
            requestDto = commentRequestDto
        )
    }

    @DeleteMapping("/delete/{commentId}")
    fun delete(
        @RequestBody @Valid deleteRequestDto: DeleteRequestDto,
        @PathVariable commentId: Long
    ): ResponseEntity<Void> {

        commentService.delete(
            commentId = commentId,
            currentURI = RequestURI.COMMENT,
            requestDto = deleteRequestDto
        )

        return ResponseEntity.ok().build()
    }
}