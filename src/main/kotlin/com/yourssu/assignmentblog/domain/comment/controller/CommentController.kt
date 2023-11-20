package com.yourssu.assignmentblog.domain.comment.controller

import com.yourssu.assignmentblog.domain.comment.dto.request.CommentRequestDto
import com.yourssu.assignmentblog.domain.comment.dto.response.CommentResponseDto
import com.yourssu.assignmentblog.domain.comment.service.CommentService
import com.yourssu.assignmentblog.global.auth.jwt.AuthInfo
import com.yourssu.assignmentblog.global.common.enums.FailedMethod
import com.yourssu.assignmentblog.global.common.enums.FailedTargetType
import com.yourssu.assignmentblog.global.common.uri.RequestURI
import com.yourssu.assignmentblog.global.util.annotation.Auth
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
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
    private val commentService: CommentService,
) {
    @PostMapping("/write/{articleId}")
    @Operation(summary = "댓글 작성", description = "댓글을 작성합니다.")
    @Parameter(name = "article id", description = "작성하고자 하는 게시글의 id 값")
    fun write(
        @RequestBody @Valid
        requestDto: CommentRequestDto,
        @PathVariable articleId: Long,
        @Auth authInfo: AuthInfo,
    ): CommentResponseDto {
        requestDto.setURIAndFailMessage(
            currentURI = RequestURI.COMMENT + "/write",
            failedTargetText = "${FailedTargetType.COMMENT} ${FailedMethod.WRITE}",
        )

        return commentService.write(
            articleId = articleId,
            requestDto = requestDto,
            email = authInfo.email,
        )
    }

    @PutMapping("/edit")
    @Operation(summary = "댓글 수정", description = "댓글을 수정합니다.")
    @Parameter(name = "article", description = "수정하고자 하는 댓글이 달린 게시글의 id 값")
    @Parameter(name = "comment", description = "수정하고자 하는 댓글의 id 값")
    fun edit(
        @RequestBody @Valid
        requestDto: CommentRequestDto,
        @RequestParam(name = "article") articleId: Long,
        @RequestParam(name = "comment") commentId: Long,
        @Auth authInfo: AuthInfo,
    ): CommentResponseDto {
        requestDto.setURIAndFailMessage(
            currentURI = RequestURI.COMMENT + "/edit?article=$articleId&comment=$commentId",
            failedTargetText = "${FailedTargetType.COMMENT} ${FailedMethod.EDIT}",
        )

        return commentService.edit(
            articleId = articleId,
            commentId = commentId,
            requestDto = requestDto,
            email = authInfo.email,
        )
    }

    @DeleteMapping("/delete")
    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.")
    @Parameter(name = "article", description = "삭제하고자 하는 댓글이 달린 게시글의 id 값")
    @Parameter(name = "comment", description = "삭제하고자 하는 댓글의 id 값")
    fun delete(
//        @RequestBody @Valid deleteRequestDto: DeleteRequestDto,
        @RequestParam(name = "article") articleId: Long,
        @RequestParam(name = "comment") commentId: Long,
        @Auth authInfo: AuthInfo,
    ): ResponseEntity<Void> {
        commentService.delete(
            articleId = articleId,
            commentId = commentId,
            currentURI = RequestURI.COMMENT,
            email = authInfo.email,
        )

        return ResponseEntity.ok().build()
    }
}
