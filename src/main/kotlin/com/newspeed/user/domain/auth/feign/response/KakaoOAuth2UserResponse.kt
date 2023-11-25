package com.newspeed.user.domain.auth.feign.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.newspeed.user.domain.auth.domain.LoginPlatform
import com.newspeed.user.domain.auth.domain.OAuth2User

@JsonIgnoreProperties(ignoreUnknown = true)
data class KakaoOAuth2UserResponse(
    val id: String,
    @JsonProperty("kakao_account")
    val kakaoAccount: KakaoAccount
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class KakaoAccount(
        val email: String,
        val profile: Profile
    ) {
        @JsonIgnoreProperties(ignoreUnknown = true)
        data class Profile(
            val nickname: String,
            @JsonProperty("thumbnail_image_url")
            val thumbnailImageUrl: String,
            @JsonProperty("profile_image_url")
            val profileImageUrl: String
        )
    }

    fun toOAuth2User(): OAuth2User = OAuth2User(
        platform = LoginPlatform.KAKAO,
        nickname = kakaoAccount.profile.nickname,
        profileImage = kakaoAccount.profile.profileImageUrl,
        email = kakaoAccount.email
    )
}