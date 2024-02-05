package com.newspeed.domain.auth.api

import com.newspeed.domain.auth.application.AppleOAuth2Service
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class AuthViewController(
    private val appleOAuth2Service: AppleOAuth2Service
) {

    @GetMapping("/login")
    fun getLoginPage(
        model: Model
    ): String {
        model.addAttribute("properties", appleOAuth2Service.getProperties())

        return "login/login"
    }
}