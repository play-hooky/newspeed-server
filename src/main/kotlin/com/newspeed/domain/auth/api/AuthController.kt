package com.newspeed.domain.auth.api

import com.newspeed.domain.auth.api.request.LoginRequest
import com.newspeed.domain.auth.api.response.LoginResponse
import com.newspeed.domain.auth.api.response.UserResponse
import com.newspeed.domain.auth.application.AuthFacade
import com.newspeed.domain.jwt.annotation.User
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/user")
class AuthController(
    private val authFacade: AuthFacade
) {

    @PostMapping("/login")
    fun kakaoLogin(
        @Valid @RequestBody loginRequest: LoginRequest
    ): ResponseEntity<LoginResponse> = ResponseEntity(
        authFacade.login(loginRequest),
        HttpStatus.CREATED
    )

    @GetMapping
    fun getUser(
        @User userId: Long
    ): ResponseEntity<UserResponse> = ResponseEntity.ok(
        authFacade.getUserResponse(userId)
    )
}