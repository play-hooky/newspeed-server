package com.newspeed.factory.auth

import com.newspeed.domain.auth.api.request.LoginRequest
import com.newspeed.domain.auth.domain.AuthPayload
import com.newspeed.domain.auth.domain.OAuth2User
import com.newspeed.domain.auth.domain.enums.LoginPlatform
import com.newspeed.domain.auth.domain.enums.Role
import com.newspeed.domain.auth.feign.request.AppleOAuth2TokenRequest
import com.newspeed.domain.auth.feign.request.KakaoOAuth2TokenRequest
import com.newspeed.domain.auth.feign.response.AppleOAuth2TokenResponse
import com.newspeed.domain.auth.feign.response.KakaoOAuth2TokenResponse
import com.newspeed.domain.auth.feign.response.KakaoOAuth2UserResponse
import com.newspeed.domain.user.domain.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts

class AuthFactory {
    companion object {
        const val DUMMY_EMAIL = "happyhooky@kakao.com"
        const val DUMMY_NICKNAME = "happyhooky"
        const val DUMMY_PROFILE_IMAGE_URL = "https://k.kakaocdn.net/14/dn/btspcwukiG5/gu6lVSl4fgjKacbmw5kZr0/o.jpg"

        const val DUMMY_ACCESS_TOKEN = "hookyhookyhookyhookyhookyhooky"
        const val DUMMY_REFRESH_TOKEN = "hookyhookyhookyhookyhookyhooky"

        const val DUMMY_APPLE_ID_TOKEN = "eyJraWQiOiJXNldjT0tCIiwiYWxnIjoiUlMyNTYifQ.eyJpc3MiOiJodHRwczovL2FwcGxlaWQuYXBwbGUuY29tIiwiYXVkIjoiY29tLm5ld3NwZWVkLnNlcnZpY2UiLCJleHAiOjE3MDE5NTg0ODYsImlhdCI6MTcwMTg3MjA4Niwic3ViIjoiMDAwNDA2LjMyYzFlNTE3YzliMTQyYzJiNmMwYmM1NzEzOWJiNThiLjA0MDMiLCJjX2hhc2giOiJFaGt6V3Z3ZGJzaURzVm5wZ1g3XzFBIiwiZW1haWwiOiJ3aGl0ZV9neXVAbmF2ZXIuY29tIiwiZW1haWxfdmVyaWZpZWQiOiJ0cnVlIiwiYXV0aF90aW1lIjoxNzAxODcyMDg2LCJub25jZV9zdXBwb3J0ZWQiOnRydWV9.sCnrdbjExTXYtDhbM12H7xkNtwmuAYibBXU_N8DOWnc64nNQP3BPdF6fMprRfcFpsuBpo5nti8sT-DuM1lVb9hleUWpQi07Sz0-BYOIUsstTL7QD0OnDzSxOjrZJrkwD-wjea9dO_NvHOWsqbJYwrEEI7aaoM5J_VFKzBk1IfFLGLAQBB77MKBw23iPGGK4CLayHLhkeaRZ3zPkBqQ4C7kBnmICDXDn6DQ3cNDakqkgCVqvVWATsrEanEJRX4n_zgFpcrGgrhSqk3taGvo5-pA-i52WeQhUqtm86ipn5EZffnDaf-cgXBxo-5indZIvChonV0Q0qTdUihq6ngx4eqA"

        fun createKakaoOauth2TokenRequest(): KakaoOAuth2TokenRequest = KakaoOAuth2TokenRequest(
            grantType = "bearer",
            clientId = DUMMY_NICKNAME,
            redirectUri = "https://www.newspeed.store",
            authorizationCode = DUMMY_ACCESS_TOKEN
        )

        fun createAppleOAuth2TokenRequest(): AppleOAuth2TokenRequest = AppleOAuth2TokenRequest(
            clientId = DUMMY_NICKNAME,
            clientSecret = DUMMY_ACCESS_TOKEN,
            authorizationCode = DUMMY_ACCESS_TOKEN,
            grantType = "bearer"
        )

        fun createKakaoOAuth2User(): OAuth2User = OAuth2User(
            platform = LoginPlatform.KAKAO,
            nickname = DUMMY_NICKNAME,
            profileImage = DUMMY_PROFILE_IMAGE_URL,
            email = DUMMY_EMAIL
        )

        fun createAppleOAuth2User(): OAuth2User = OAuth2User(
            platform = LoginPlatform.APPLE,
            nickname = DUMMY_NICKNAME,
            profileImage = null,
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

        fun createAppleOAuth2TokenResponse() = AppleOAuth2TokenResponse(
            tokenType = "bearer",
            accessToken = DUMMY_ACCESS_TOKEN,
            expiresIn = 21599,
            idToken = DUMMY_APPLE_ID_TOKEN
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

        fun createAppleJwtClaims(): Claims {
            val claims = Jwts.claims()

            claims[AuthPayload.USER_ID_KEY] = 10
            claims[AuthPayload.ROLE_KEY] = Role.USER
            claims[AuthPayload.PLATFORM_KEY] = LoginPlatform.NEWSPEED
            claims[AuthPayload.EMAIL_KEY] = DUMMY_EMAIL

            return claims
        }

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

        fun createAuthPayload(
            role: Role
        ): AuthPayload = AuthPayload(
            userId = 1,
            role = role,
            loginPlatform = LoginPlatform.NEWSPEED,
            email = DUMMY_EMAIL
        )
    }
}