package com.newspeed.domain.alarm.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.newspeed.domain.alarm.api.request.AlarmSaveRequest
import com.newspeed.domain.alarm.api.response.AlarmResponse
import com.newspeed.domain.alarm.application.AlarmService
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.nio.charset.StandardCharsets

@DisplayName("알람 관련 API 내")
@CustomWebMvcTestTemplate
class AlarmControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var alarmService: AlarmService

    @Nested
    inner class `알람 저장 API에서` {

        @Test
        @WithMockAuth
        fun `알람 시작 시간이 존재하지 않으면 에러를 던진다`() {
            // given
            val requestBody = AlarmSaveRequest(
                startTime = "",
                endTime = "01:00"
            )

            // when
            val result = mockMvc.perform(post("/user/alarm")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            ).andExpect(status().isBadRequest)
                .andReturn()
                .response
                .getContentAsString(StandardCharsets.UTF_8)

            // then
            val actual = objectMapper.readValue(result, ExceptionResponse::class.java)
            Assertions.assertThat(actual.errorMessage).contains("알람 시작 시간")
        }

        @Test
        @WithMockAuth
        fun `알람 종료 시간이 존재하지 않으면 에러를 던진다`() {
            // given
            val requestBody = AlarmSaveRequest(
                startTime = "01:00",
                endTime = ""
            )

            // when
            val result = mockMvc.perform(post("/user/alarm")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            ).andExpect(status().isBadRequest)
                .andReturn()
                .response
                .getContentAsString(StandardCharsets.UTF_8)

            // then
            val actual = objectMapper.readValue(result, ExceptionResponse::class.java)
            Assertions.assertThat(actual.errorMessage).contains("알람 종료 시간")
        }

        @Test
        fun `비회원이 접근하면 에러를 던진다`() {
            // given
            val requestBody = AlarmSaveRequest(
                startTime = "01:00",
                endTime = "05:00"
            )

            // when & then
            val result = mockMvc.perform(post("/user/alarm")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            ).andExpect(status().isBadRequest)
        }

        @Test
        @WithMockAuth
        fun `알람 저장 요청에 성공한다`() {
            // given
            val requestBody = AlarmSaveRequest(
                startTime = "01:00",
                endTime = "05:00"
            )

            // when
            mockMvc.perform(post("/user/alarm")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            ).andExpect(status().isCreated)
        }
    }

    @Nested
    inner class `알람 조회 API에서` {

        @Test
        fun `비회원이 접근하면 에러를 던진다`() {
            // when & then
            mockMvc.perform(get("/user/alarm")
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isBadRequest)
        }

        @Test
        @WithMockAuth
        fun `알람 조회에 성공한다`() {
            // given
            val expected = AlarmResponse(
                startTime = "01:00",
                endTime = "05:00"
            )

            given(alarmService.getAlarm(1L))
                .willReturn(expected)

            // when
            val result = mockMvc.perform(get("/user/alarm")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            ).andExpect(status().isOk)
                .andReturn()
                .response
                .getContentAsString(StandardCharsets.UTF_8)

            val actual = objectMapper.readValue(result, AlarmResponse::class.java)

            // then
            Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected)
        }
    }

    @Nested
    inner class `알람 수정 API에서` {

        @Test
        @WithMockAuth
        fun `알람 시작 시간이 존재하지 않으면 에러를 던진다`() {
            // given
            val requestBody = AlarmSaveRequest(
                startTime = "",
                endTime = "01:00"
            )

            // when
            val result = mockMvc.perform(put("/user/alarm")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            ).andExpect(status().isBadRequest)
                .andReturn()
                .response
                .getContentAsString(StandardCharsets.UTF_8)

            // then
            val actual = objectMapper.readValue(result, ExceptionResponse::class.java)
            Assertions.assertThat(actual.errorMessage).contains("알람 시작 시간")
        }

        @Test
        @WithMockAuth
        fun `알람 종료 시간이 존재하지 않으면 에러를 던진다`() {
            // given
            val requestBody = AlarmSaveRequest(
                startTime = "01:00",
                endTime = ""
            )

            // when
            val result = mockMvc.perform(put("/user/alarm")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            ).andExpect(status().isBadRequest)
                .andReturn()
                .response
                .getContentAsString(StandardCharsets.UTF_8)

            // then
            val actual = objectMapper.readValue(result, ExceptionResponse::class.java)
            Assertions.assertThat(actual.errorMessage).contains("알람 종료 시간")
        }

        @Test
        fun `비회원이 접근하면 에러를 던진다`() {
            // when & then
            mockMvc.perform(put("/user/alarm")
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isBadRequest)
        }

        @Test
        @WithMockAuth
        fun `알람 수정 요청에 성공한다`() {
            // given
            val requestBody = AlarmSaveRequest(
                startTime = "01:00",
                endTime = "05:00"
            )

            // when & then
            mockMvc.perform(put("/user/alarm")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            ).andExpect(status().isCreated)
                .andReturn()
                .response
                .getContentAsString(StandardCharsets.UTF_8)
        }
    }

    @Nested
    inner class `알람 삭제 API에서` {

        @Test
        fun `비회원이 접근하면 에러를 던진다`() {
            // when & then
            mockMvc.perform(delete("/user/alarm")
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isBadRequest)
        }

        @Test
        @WithMockAuth
        fun `알람 삭제에 성공한다`() {
            // when & then
            mockMvc.perform(
                delete("/user/alarm")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            ).andExpect(status().isOk)
        }
    }
}