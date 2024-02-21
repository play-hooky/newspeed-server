package com.newspeed.domain.auth.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.newspeed.domain.auth.api.request.LoginRequest
import com.newspeed.domain.auth.api.request.WithdrawalRequest
import com.newspeed.domain.auth.api.response.LoginResponse
import com.newspeed.domain.auth.api.response.UserResponse
import com.newspeed.domain.auth.application.AuthFacade
import com.newspeed.domain.auth.domain.enums.LoginPlatform
import com.newspeed.factory.auth.AuthFactory.Companion.DUMMY_EMAIL
import com.newspeed.factory.auth.AuthFactory.Companion.DUMMY_NICKNAME
import com.newspeed.factory.auth.AuthFactory.Companion.DUMMY_PROFILE_IMAGE_URL
import com.newspeed.global.exception.model.ExceptionResponse
import com.newspeed.template.CustomWebMvcTestTemplate
import com.newspeed.util.WithMockAuth
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.nio.charset.StandardCharsets

@DisplayName("인증 인가 관련 컨트롤러 내")
@CustomWebMvcTestTemplate
class AuthControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var authFacade: AuthFacade

    @Nested
    inner class `로그인 API에서` {

        @Test
        fun `authorization code를 입력하지 않으면 에러를 던진다`() {
            // given
            val requestBody = LoginRequest(
                authorizationCode = "",
                loginPlatform = LoginPlatform.NEWSPEED
            )

            // when
            val result = mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
            ).andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andReturn()
                .response
                .getContentAsString(StandardCharsets.UTF_8)

            // then
            val actual = objectMapper.readValue(result, ExceptionResponse::class.java)
            Assertions.assertThat(actual.errorMessage).contains("authorization code")
        }

        @Test
        fun `정상 입력값을 입력하면 로그인에 성공한다`() {
            // given
            val requestBody = LoginRequest(
                authorizationCode = "authorization code",
                loginPlatform = LoginPlatform.NEWSPEED
            )
            val expected = LoginResponse(
                refreshToken = "refresh token",
                accessToken = "access token",
                userId = 1L
            )

            given(authFacade.login(requestBody))
                .willReturn(expected)

            // when
            val result = mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
            ).andExpect(MockMvcResultMatchers.status().isCreated)
                .andReturn()
                .response
                .getContentAsString(StandardCharsets.UTF_8)

            // then
            val actual = objectMapper.readValue(result, LoginResponse::class.java)
            Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected)
        }
    }

    @Nested
    inner class `로그아웃 API에서` {

        @Test
        fun `비회원이면 에러를 던진다`() {
            // when & then
            mockMvc.perform(post("/user/logout")
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isBadRequest)
        }

        @Test
        @WithMockAuth
        fun `회원이면 로그아웃에 성공한다`() {
            // when & then
            mockMvc.perform(post("/user/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            ).andExpect(status().isOk)
        }
    }

    @Nested
    inner class `사용자 조회 API에서` {

        @Test
        fun `비회원이면 에러를 던진다`() {
            // when & then
            mockMvc.perform(get("/user")
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isBadRequest)
        }

        @Test
        @WithMockAuth
        fun `회원이면 회원 정보 조회에 성공한다`() {
            // given
            val expected = UserResponse(
                email = DUMMY_EMAIL,
                nickname = DUMMY_NICKNAME,
                profileImgUrl = DUMMY_PROFILE_IMAGE_URL
            )

            given(authFacade.getUserResponse(1L))
                .willReturn(expected)

            // when
            val result = mockMvc.perform(get("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,"Bearer token")
            ).andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()
                .response
                .getContentAsString(StandardCharsets.UTF_8)

            val actual = objectMapper.readValue(result, UserResponse::class.java)
            Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected)
        }
    }

    @Nested
    inner class `access token 재발행 API에서` {

        @Test
        fun `비회원이면 에러를 던진다`() {
            // when & then
            mockMvc.perform(put("/user/token")
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isBadRequest)
        }

        @Test
        @WithMockAuth
        fun `회원이면 access token 재발행에 성공한다`() {
            // given
            val expected = "new refresh token"

            given(authFacade.reissueAccessToken(1L))
                .willReturn(expected)

            // when
            val actual = mockMvc.perform(put("/user/token")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,"Bearer token")
            ).andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()
                .response
                .getContentAsString(StandardCharsets.UTF_8)

            // then
            Assertions.assertThat(actual).isEqualTo(expected)
        }
    }

    @Nested
    inner class `탈퇴 API에서` {

        @Test
        fun `비회원이면 에러를 던진다`() {
            // when & then
            mockMvc.perform(post("/user/withdrawal")
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isBadRequest)
        }

        @Test
        @WithMockAuth
        fun `authorization code를 입력하지 않으면 에러를 던진다`() {
            // given
            val requestBody = WithdrawalRequest(
                authorizationCode = ""
            )

            // when
            val result = mockMvc.perform(post("/user/withdrawal")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,"Bearer token")
                .content(objectMapper.writeValueAsString(requestBody))
            ).andExpect(status().isBadRequest)
                .andReturn()
                .response
                .getContentAsString(StandardCharsets.UTF_8)

            val actual = objectMapper.readValue(result, ExceptionResponse::class.java)
            Assertions.assertThat(actual.errorMessage).contains("authorization code")
        }

        @Test
        @WithMockAuth
        fun `회원이 정상 입력하면 탈퇴에 성공한다`() {
            // given
            val requestBody = WithdrawalRequest(
                authorizationCode = "authorization code"
            )

            // when
            mockMvc.perform(post("/user/withdrawal")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,"Bearer token")
                .content(objectMapper.writeValueAsString(requestBody))
            ).andExpect(status().isOk)
                .andReturn()
                .response
                .getContentAsString(StandardCharsets.UTF_8)
        }
    }
}