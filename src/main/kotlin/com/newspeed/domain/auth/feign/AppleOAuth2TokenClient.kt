package com.newspeed.domain.auth.feign

import com.newspeed.domain.auth.feign.response.AppleOAuth2PublicKeyResponse
import com.newspeed.domain.auth.feign.response.AppleOAuth2TokenResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam


@FeignClient(name = "apple-oauth2-token-client", url = "\${oauth.apple.url}")
interface AppleOAuth2TokenClient {

    @PostMapping("/auth/token")
    fun getOAuthAppleToken(
        @RequestParam("client_id") clientId: String,
        @RequestParam("client_secret") clientSecret: String,
        @RequestParam("grant_type") grantType: String,
        @RequestParam("code") code: String
    ): AppleOAuth2TokenResponse

    @GetMapping("/auth/keys")
    fun getApplePublicKeys(): AppleOAuth2PublicKeyResponse

    @PostMapping("/auth/revoke")
    fun revoke(
        @RequestParam("client_id") clientId: String,
        @RequestParam("client_secret") clientSecret: String,
        @RequestParam("token") token: String,
        @RequestParam("token_type_hint") tokenTypeHint: String,
    )
}