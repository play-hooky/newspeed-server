package com.newspeed.domain.content.api

import com.newspeed.domain.auth.domain.AuthenticateContext
import com.newspeed.domain.content.api.request.ContentSaveRequest
import com.newspeed.domain.content.api.request.ContentSearchRequest
import com.newspeed.domain.content.api.response.ContentSearchResponse
import com.newspeed.domain.content.api.response.QueryHistoryResponse
import com.newspeed.domain.content.api.response.RecommendQueryResponse
import com.newspeed.domain.content.application.ContentService
import com.newspeed.domain.jwt.annotation.User
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import javax.validation.Valid

@Validated
@RestController
@RequestMapping("/contents")
class ContentController(
    private val contentService: ContentService,
    private val authenticateContext: AuthenticateContext
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

    @GetMapping("/search")
    fun searchContents(
        @Valid request: ContentSearchRequest
    ): ResponseEntity<ContentSearchResponse> = ResponseEntity.ok(
        if (authenticateContext.hasAnonymous()) contentService.search(request)
        else contentService.search(authenticateContext.userId(), request)
    )

    @GetMapping("/query/history")
    fun getQueryHistory(
        @User userId: Long
    ): ResponseEntity<QueryHistoryResponse> = ResponseEntity.ok(
        contentService.getQueryHistory(userId)
    )

    @GetMapping("/query/recommend")
    fun recommendQuery(
        @RequestParam(required = false, defaultValue = "#{T(java.time.LocalDate).now()}") date: LocalDate,
        @RequestParam(required = false, defaultValue = "5") size: Int
    ): ResponseEntity<RecommendQueryResponse> = ResponseEntity.ok(
        contentService.recommendQuery(date, size)
    )

    @DeleteMapping("/{queryHistoryId}")
    fun deleteQueryHistory(
        @User userId: Long,
        @PathVariable("queryHistoryId") queryHistoryId: Long
    ): ResponseEntity<Unit> {
        contentService.deleteQueryHistory(userId, queryHistoryId)

        return ResponseEntity(HttpStatus.OK)
    }
}