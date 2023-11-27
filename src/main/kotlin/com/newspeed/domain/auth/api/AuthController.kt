package com.newspeed.domain.auth.api

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class AuthController(
    private val authFacade: com.newspeed.domain.auth.application.AuthFacade
) {

    @PostMapping("/login/kakao")
    fun kakaoLogin(
        @Valid @RequestBody kakaoLoginRequest: com.newspeed.domain.auth.api.request.KakaoLoginRequest
    ): ResponseEntity<com.newspeed.domain.auth.api.response.KakaoLoginResponse> = ResponseEntity(
        authFacade.kakaoLogin(kakaoLoginRequest),
        HttpStatus.CREATED
    )
}