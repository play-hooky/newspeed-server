package com.newspeed.domain.auth.api

import com.newspeed.domain.auth.api.request.KakaoLoginRequest
import com.newspeed.domain.auth.api.response.KakaoLoginResponse
import com.newspeed.domain.auth.application.AuthFacade
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class AuthController(
    private val authFacade: AuthFacade
) {

    @PostMapping("/user/login/kakao")
    fun kakaoLogin(
        @Valid @RequestBody kakaoLoginRequest: KakaoLoginRequest
    ): ResponseEntity<KakaoLoginResponse> = ResponseEntity(
        authFacade.kakaoLogin(kakaoLoginRequest),
        HttpStatus.CREATED
    )
}