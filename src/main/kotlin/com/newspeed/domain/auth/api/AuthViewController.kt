package com.newspeed.domain.auth.api

import com.newspeed.domain.auth.config.AppleOAuth2ConfigProperties
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class AuthViewController(
    private val appleOAuth2ConfigProperties: AppleOAuth2ConfigProperties
) {

    @GetMapping("/login")
    fun getLoginPage(
        model: Model
    ): String {
        model.addAttribute("properties", appleOAuth2ConfigProperties)

        return "login/login"
    }
}