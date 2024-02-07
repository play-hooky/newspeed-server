package com.newspeed.domain.auth.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.newspeed.domain.auth.api.request.LoginRequest
import com.newspeed.domain.auth.domain.enums.LoginPlatform
import com.newspeed.global.exception.handler.GlobalExceptionHandler
import com.newspeed.support.MockUser
import com.newspeed.template.IntegrationTestTemplate
import io.kotest.core.annotation.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@DisplayName("AuthController가 제공하는 API 내")
class AuthControllerTest(
    @Autowired val objectMapper: ObjectMapper,
    @Autowired val authController: AuthController,
    @Autowired val globalExceptionHandler: GlobalExceptionHandler
): IntegrationTestTemplate {
    val mockMvc = MockMvcBuilders
        .standaloneSetup(authController)
        .setControllerAdvice(globalExceptionHandler)
        .build()

    @Nested
    inner class `login API에서` {

        @Test
        fun `authorization_code를 입력하지 않으면 bean validation exception을 던진다`() {
            // given
            val loginRequest = LoginRequest(
                authorizationCode = "",
                loginPlatform = LoginPlatform.NEWSPEED
            )

            // when & then
            mockMvc.post("/user/login") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(loginRequest)
                accept = MediaType.APPLICATION_JSON
            }.andExpect {
                status { isBadRequest() }
            }
        }

        @Test
        fun `카카오 로그인에 유효하지 않은 authorization_code를 입력하면 exception을 던진다`() {
            // given
            val loginRequest = LoginRequest(
                authorizationCode = "bad authorization code",
                loginPlatform = LoginPlatform.KAKAO
            )

            // when & then
            mockMvc.post("/user/login") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(loginRequest)
                accept = MediaType.APPLICATION_JSON
            }.andExpect {
                status { isInternalServerError() }
            }
        }

        @Test
        fun `애플 로그인에 유효하지 않은 authorization_code를 입력하면 exception을 던진다`() {
            // given
            val loginRequest = LoginRequest(
                authorizationCode = "bad authorization code",
                loginPlatform = LoginPlatform.APPLE
            )

            // when & then
            mockMvc.post("/user/login") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(loginRequest)
                accept = MediaType.APPLICATION_JSON
            }.andExpect {
                status { isInternalServerError() }
            }
        }

        @Test
        fun `애플 로그인에 이메일 제공 동의를 하지 않으면 UnavailableEmailException을 던진다`() {
            // todo
        }

        @Test
        fun `로그인에 성공하면 사용자 정보와 발급한 JWT를 저장한다`() {
            // given
            val loginRequest = LoginRequest(
                authorizationCode = "authorization code",
                loginPlatform = LoginPlatform.NEWSPEED
            )

            // when & then
            mockMvc.post("/user/login") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(loginRequest)
                accept = MediaType.APPLICATION_JSON
            }.andExpect {
                status { isCreated() }
            }
        }
    }

    @Nested
    inner class `logout API에서` {

        @Test
        @MockUser
        fun `로그아웃에 성공하면 refresh token을 삭제한다`() {
            // given
            val authorization = "hooky"

            // when & then
            mockMvc.post("/user/logout") {
                contentType = MediaType.APPLICATION_JSON
                header("authorization", "Bearer $authorization")
                accept = MediaType.APPLICATION_JSON
            }.andExpect {
                status { isOk() }
            }
        }

        @Test
        fun `사용자를 식별할 수 없으면 로그아웃에 실패한다`() {
            // given
            val authorization = ""

            // when & then
            mockMvc.post("/user/logout") {
                contentType = MediaType.APPLICATION_JSON
                header("authorization", "Bearer $authorization")
                accept = MediaType.APPLICATION_JSON
            }.andExpect {
                status { isBadRequest() }
            }
        }
    }

    @Nested
    inner class `사용자 조회 API에서` {

        @Test
        fun `a`() {

        }

        @Test
        fun `b`() {

        }
    }

    @Nested
    inner class `access token 재발행 API에서` {

        @Test
        fun `a`() {

        }

        @Test
        fun `ab`() {

        }
    }

    @Nested
    inner class `회원 탈퇴 API에서` {

        @Test
        fun `a`() {

        }

        @Test
        fun `ab`() {

        }
    }
}