package com.newspeed.domain.queryhistory.api

import com.newspeed.domain.jwt.annotation.User
import com.newspeed.domain.queryhistory.api.response.QueryHistoryResponse
import com.newspeed.domain.queryhistory.api.response.RecommendQueryResponse
import com.newspeed.domain.queryhistory.application.QueryHistoryService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@Validated
@RestController
@RequestMapping("/contents")
class QueryHistoryController(
    private val queryHistoryService: QueryHistoryService
) {

    @GetMapping("/query/history")
    fun getQueryHistory(
        @User userId: Long
    ): ResponseEntity<QueryHistoryResponse> = ResponseEntity.ok(
        queryHistoryService.getQueryHistory(userId)
    )

    @GetMapping("/query/recommend")
    fun recommendQuery(
        @RequestParam(required = false, defaultValue = "#{T(java.time.LocalDate).now()}") date: LocalDate,
        @RequestParam(required = false, defaultValue = "5") size: Int
    ): ResponseEntity<RecommendQueryResponse> = ResponseEntity.ok(
        queryHistoryService.recommendQuery(date, size)
    )

    @DeleteMapping("/query/history/{queryHistoryId}")
    fun deleteQueryHistory(
        @User userId: Long,
        @PathVariable("queryHistoryId") queryHistoryId: Long
    ): ResponseEntity<Unit> {
        queryHistoryService.deleteQueryHistory(userId, queryHistoryId)

        return ResponseEntity(HttpStatus.OK)
    }
}