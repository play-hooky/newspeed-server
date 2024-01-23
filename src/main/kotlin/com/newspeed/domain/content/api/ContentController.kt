package com.newspeed.domain.content.api

import com.newspeed.domain.content.api.request.ContentDeleteRequest
import com.newspeed.domain.content.api.request.ContentSaveRequest
import com.newspeed.domain.content.api.response.ContentsResponse
import com.newspeed.domain.content.application.ContentService
import com.newspeed.domain.jwt.annotation.User
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@Validated
@RestController
@RequestMapping("/contents")
class ContentController(
    private val contentService: ContentService
) {

    @PostMapping
    fun saveContent(
        @User userId: Long,
        @Valid @RequestBody request: ContentSaveRequest
    ): ResponseEntity<Unit> {
        contentService.saveContent(
            request.toCommand(userId)
        )
        return ResponseEntity(HttpStatus.CREATED)
    }

    @GetMapping
    fun getContents(
        @User userId: Long
    ): ResponseEntity<ContentsResponse> = ResponseEntity.ok(
        contentService.getContents(userId)
    )

    @DeleteMapping
    fun deleteContent(
        @User userId: Long,
        @Valid @RequestBody request: ContentDeleteRequest
    ): ResponseEntity<Unit> {
        contentService.deleteContent(
            request.toCommand(userId)
        )

        return ResponseEntity(HttpStatus.OK)
    }
}