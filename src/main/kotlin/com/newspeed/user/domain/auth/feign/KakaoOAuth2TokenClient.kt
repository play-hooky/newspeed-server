package com.newspeed.user.domain.auth.feign

import com.newspeed.user.domain.auth.feign.request.KakaoOAuth2TokenRequest
import com.newspeed.user.domain.auth.feign.response.KakaoOAuth2TokenResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.cloud.openfeign.SpringQueryMap
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(name = "kakao-oauth2-token-client", url = "\${oauth.kakao.auth-url}")
interface KakaoOAuth2TokenClient {
    @PostMapping("/oauth/token")
    fun getOAuthKakaoToken(@SpringQueryMap request: KakaoOAuth2TokenRequest): KakaoOAuth2TokenResponse
}