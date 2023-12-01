package com.newspeed.factory.auth

import com.newspeed.domain.auth.api.request.LoginRequest
import com.newspeed.domain.auth.domain.enums.LoginPlatform
import com.newspeed.domain.auth.domain.OAuth2User
import com.newspeed.domain.auth.domain.enums.Role
import com.newspeed.domain.auth.feign.request.KakaoOAuth2TokenRequest
import com.newspeed.domain.auth.feign.response.KakaoOAuth2TokenResponse
import com.newspeed.domain.auth.feign.response.KakaoOAuth2UserResponse
import com.newspeed.domain.user.domain.User

class AuthFactory {
    companion object {
        const val DUMMY_EMAIL = "happyhooky@kakao.com"
        const val DUMMY_NICKNAME = "happyhooky"
        const val DUMMY_PROFILE_IMAGE_URL = "https://k.kakaocdn.net/14/dn/btspcwukiG5/gu6lVSl4fgjKacbmw5kZr0/o.jpg"

        const val DUMMY_ACCESS_TOKEN = "hookyhookyhookyhookyhookyhooky"
        const val DUMMY_REFRESH_TOKEN = "hookyhookyhookyhookyhookyhooky"

        fun createKakaoOauth2TokenRequest(): KakaoOAuth2TokenRequest = KakaoOAuth2TokenRequest(
            grantType = "bearer",
            clientId = DUMMY_NICKNAME,
            redirectUri = "https://www.newspeed.store",
            authorizationCode = DUMMY_ACCESS_TOKEN
        )


        fun createKakaoOAuth2User(): OAuth2User = OAuth2User(
            platform = LoginPlatform.KAKAO,
            nickname = DUMMY_NICKNAME,
            profileImage = DUMMY_PROFILE_IMAGE_URL,
            email = DUMMY_EMAIL
        )

        fun createKakaoOAuth2TokenResponse() = KakaoOAuth2TokenResponse(
            tokenType = "bearer",
            accessToken = DUMMY_ACCESS_TOKEN,
            refreshToken = DUMMY_REFRESH_TOKEN,
            expiresIn = 21599,
            scope = "account_email profile_image profile_nickname",
            refreshTokenExpiresIn = 5183999
        )

        fun createKakaoOAuth2UserResponse() = KakaoOAuth2UserResponse(
            id = "2945247360",
            kakaoAccount = KakaoOAuth2UserResponse.KakaoAccount(
                email = DUMMY_EMAIL,
                profile = KakaoOAuth2UserResponse.KakaoAccount.Profile(
                    nickname = DUMMY_NICKNAME,
                    thumbnailImageUrl = DUMMY_PROFILE_IMAGE_URL,
                    profileImageUrl = DUMMY_PROFILE_IMAGE_URL
                )
            )
        )

        fun createKakaoUser(): User = User(
            email = DUMMY_EMAIL,
            nickname = DUMMY_NICKNAME,
            platform = LoginPlatform.KAKAO,
            profileImageUrl = DUMMY_PROFILE_IMAGE_URL,
            role = Role.USER
        )

        fun createKakaoLoginRequest(): LoginRequest = LoginRequest(
            authorizationCode = DUMMY_ACCESS_TOKEN,
            loginPlatform = LoginPlatform.NEWSPEED
        )
    }
}