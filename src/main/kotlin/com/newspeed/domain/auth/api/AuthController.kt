package com.newspeed.domain.auth.api

import com.newspeed.domain.auth.api.request.LoginRequest
import com.newspeed.domain.auth.api.response.LoginResponse
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

    @PostMapping("/user/login")
    fun kakaoLogin(
        @Valid @RequestBody loginRequest: LoginRequest
    ): ResponseEntity<LoginResponse> = ResponseEntity(
        authFacade.login(loginRequest),
        HttpStatus.CREATED
    )
}