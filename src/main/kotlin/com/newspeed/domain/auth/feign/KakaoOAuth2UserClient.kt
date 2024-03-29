package com.newspeed.domain.auth.feign

import com.newspeed.domain.auth.feign.response.KakaoOAuth2UnlinkResponse
import com.newspeed.domain.auth.feign.response.KakaoOAuth2UserResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(name = "kakao-oauth2-user-client", url = "\${oauth.kakao.user-url}")
interface KakaoOAuth2UserClient {
    @GetMapping("/v2/user/me")
    fun getKakaoUserEntity(@RequestHeader(value = "Authorization") authorization: String): KakaoOAuth2UserResponse

    @PostMapping("/v1/user/unlink")
    fun unlink(@RequestHeader(value = "Authorization") authorization: String): KakaoOAuth2UnlinkResponse
}