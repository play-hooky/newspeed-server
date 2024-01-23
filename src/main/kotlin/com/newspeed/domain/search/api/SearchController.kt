package com.newspeed.domain.search.api

import com.newspeed.domain.auth.domain.AuthenticateContext
import com.newspeed.domain.search.api.request.ContentSearchRequest
import com.newspeed.domain.search.api.response.ContentSearchResponse
import com.newspeed.domain.search.application.SearchService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/contents")
class SearchController(
    private val authenticateContext: AuthenticateContext,
    private val searchService: SearchService
) {

    @GetMapping("/search")
    fun searchContents(
        @Valid request: ContentSearchRequest
    ): ResponseEntity<ContentSearchResponse> = ResponseEntity.ok(
        if (authenticateContext.hasAnonymous()) searchService.search(request)
        else searchService.search(authenticateContext.userId(), request)
    )
}