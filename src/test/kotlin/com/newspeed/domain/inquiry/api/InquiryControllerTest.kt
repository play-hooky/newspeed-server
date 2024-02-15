package com.newspeed.domain.inquiry.api

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.newspeed.domain.inquiry.api.request.InquiryQuestionRequest
import com.newspeed.domain.inquiry.api.response.InquiryResponse
import com.newspeed.domain.inquiry.application.InquiryService
import com.newspeed.template.CustomWebMvcTestTemplate
import com.newspeed.util.WithMockAuth
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import kotlin.math.pow

@CustomWebMvcTestTemplate
@DisplayName("문의 사항 API 내")
class InquiryControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var inquiryService: InquiryService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Nested
    inner class `문의 사항을 등록할 때` {

        @Test
        fun `비회원이면 에러를 던진다`() {
            // given
            val requestBody = InquiryQuestionRequest(
                title = "title",
                body = "body"
            )

            // when & then
            mockMvc.perform(post("/user/inquiry")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            ).andExpect(MockMvcResultMatchers.status().isBadRequest)
        }

        @Test
        @WithMockAuth
        fun `문의 내용 제목을 입력하지 않으면 에러를 던진다`() {
            // given
            val requestBody = InquiryQuestionRequest(
                title = "",
                body = "body"
            )

            // when & then
            mockMvc.perform(post("/user/inquiry")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            ).andExpect(MockMvcResultMatchers.status().isBadRequest)
        }

        @Test
        @WithMockAuth
        fun `문의 내용 본문을 입력하지 않으면 에러를 던진다`() {
            // given
            val requestBody = InquiryQuestionRequest(
                title = "title",
                body = ""
            )

            // when & then
            mockMvc.perform(post("/user/inquiry")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            ).andExpect(MockMvcResultMatchers.status().isBadRequest)
        }

        @Test
        @WithMockAuth
        fun `문의 내용 본문 길이를 초과하면 에러를 던진다`() {
            // given
            val longString = buildString {
                repeat(2.0.pow(17).toInt()) { append("A") }
            }
            val requestBody = InquiryQuestionRequest(
                title = "title",
                body = longString
            )

            // when & then
            mockMvc.perform(post("/user/inquiry")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            ).andExpect(MockMvcResultMatchers.status().isBadRequest)
        }

        @Test
        @WithMockAuth
        fun `유효한 요청값을 입력하면 등록에 성공한다`() {
            // given
            val requestBody = InquiryQuestionRequest(
                title = "title",
                body = "body"
            )

            // when & then
            mockMvc.perform(post("/user/inquiry")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            ).andExpect(MockMvcResultMatchers.status().isCreated)
        }
    }

    @Nested
    inner class `문의 사항을 조회할 때` {

        @Test
        fun `비회원이면 에러를 던진다`() {
            // when & then
            mockMvc.perform(get("/user/inquiry")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            ).andExpect(MockMvcResultMatchers.status().isBadRequest)
        }

        @Test
        @WithMockAuth
        fun `회원이면 결과를 반환한다`() {
            // given
            val expected = InquiryResponse(
                inquiries = listOf(InquiryResponse.InquiryDTO(
                    question = InquiryResponse.InquiryDTO.InquiryQuestionDTO(
                        title = "title",
                        body = "body",
                        createdAt = LocalDateTime.now().minusMinutes(30)
                    ),
                    answer = null
                ))
            )

            given(inquiryService.getInquiry(1L))
                .willReturn(expected)

            // when
            val result = mockMvc.perform(get("/user/inquiry")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            ).andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()
                .response
                .getContentAsString(StandardCharsets.UTF_8)

            val actual = objectMapper.readValue(result, object:TypeReference<List<InquiryResponse.InquiryDTO>>() {})

            assertThat(actual).usingRecursiveComparison().isEqualTo(expected.inquiries)
        }
    }
}