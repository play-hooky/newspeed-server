package com.newspeed.domain.content.api

import com.newspeed.domain.auth.domain.AuthenticateContext
import com.newspeed.domain.content.api.request.ContentSearchRequest
import com.newspeed.domain.content.api.response.ContentSearchResponse
import com.newspeed.domain.content.application.ContentService
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@Validated
class ContentController(
    private val contentService: ContentService
) {

    @GetMapping("/contents/search")
    fun searchContents(
        authenticateContext: AuthenticateContext,
        @Valid request: ContentSearchRequest
    ): ResponseEntity<ContentSearchResponse> = ResponseEntity.ok(
        if (authenticateContext.hasAnonymous()) contentService.search(request)
        else contentService.search(authenticateContext.userId(), request)
    )
}