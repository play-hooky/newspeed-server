package com.newspeed.domain.content.api

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.newspeed.domain.content.api.request.ContentDeleteRequest
import com.newspeed.domain.content.api.request.ContentSaveRequest
import com.newspeed.domain.content.api.response.ContentsResponse
import com.newspeed.domain.content.application.ContentService
import com.newspeed.domain.content.domain.enums.QueryPlatform
import com.newspeed.domain.content.dto.ContentResponseDTO
import com.newspeed.factory.content.ContentFactory
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
import java.nio.charset.StandardCharsets

@CustomWebMvcTestTemplate
@DisplayName("컨텐츠 컨트롤러 내")
class ContentControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var contentService: ContentService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Nested
    inner class `컨텐츠 저장 API에서` {

        @Test
        fun `비회원이면 에러를 던진다`() {
            // given
            val requestBody = ContentSaveRequest(
                contentIdInPlatform = "id",
                url = "https://www.newspeed.store"
            )

            // when & then
            mockMvc.perform(post("/contents")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
            ).andExpect(MockMvcResultMatchers.status().isBadRequest)
        }

        @Test
        @WithMockAuth
        fun `컨텐츠 ID를 입력하지 않으면 에러를 던진다`() {
            // given
            val requestBody = ContentSaveRequest(
                contentIdInPlatform = "",
                url = "https://www.newspeed.store"
            )

            // when & then
            val result = mockMvc.perform(post("/contents")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestBody))
                    .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            ).andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andReturn()
                .response
                .getContentAsString(StandardCharsets.UTF_8)

            val actual = objectMapper.readValue(result, ExceptionResponse::class.java)
            Assertions.assertThat(actual.errorMessage).contains("contentIdInPlatform")
        }

        @Test
        @WithMockAuth
        fun `컨텐츠 url을 입력하지 않으면 에러를 던진다`() {
            // given
            val requestBody = ContentSaveRequest(
                contentIdInPlatform = "id",
                url = ""
            )

            // when & then
            val result = mockMvc.perform(post("/contents")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestBody))
                    .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            ).andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andReturn()
                .response
                .getContentAsString(StandardCharsets.UTF_8)

            val actual = objectMapper.readValue(result, ExceptionResponse::class.java)
            Assertions.assertThat(actual.errorMessage).contains("url")
        }

        @Test
        @WithMockAuth
        fun `정상 정보를 입력하면 저장에 성공한다`() {
            // given
            val requestBody = ContentSaveRequest(
                contentIdInPlatform = "id",
                url = "https://www.newspeed.store"
            )

            // when & then
            mockMvc.perform(post("/contents")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestBody))
                    .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            ).andExpect(MockMvcResultMatchers.status().isCreated)
                .andReturn()
                .response
                .getContentAsString(StandardCharsets.UTF_8)
        }
    }

    @Nested
    inner class `컨텐츠 조회 API에서` {

        @Test
        fun `비회원이면 에러를 던진다`() {
            // when & then
            mockMvc.perform(get("/contents")
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.status().isBadRequest)
        }

        @Test
        @WithMockAuth
        fun `회원이면 조회에 성공한다`() {
            // given
            val expected = ContentsResponse(
                contentResponses = ContentFactory.createContentResponseDTOs(QueryPlatform.NEWSPEED)
            )

            given(contentService.getContents(1L))
                .willReturn(expected)

            // when
            val result = mockMvc.perform(get("/contents")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                ).andExpect(MockMvcResultMatchers.status().isOk)
                    .andReturn()
                    .response
                    .getContentAsString(StandardCharsets.UTF_8)

            val actual = objectMapper.readValue(result, object :TypeReference<List<ContentResponseDTO>>() {})
            Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected.contentResponses)
        }
    }

    @Nested
    inner class `컨텐츠 삭제 API에서` {

        @Test
        fun `비회원이면 에러를 던진다`() {
            // when & then
            mockMvc.perform(delete("/contents")
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.status().isBadRequest)
        }

        @Test
        @WithMockAuth
        fun `삭제하려는 컨텐츠 url을 입력하지 않으면 에러를 던진다`() {
            // given
            val requestBody = ContentDeleteRequest(
                url = ""
            )

            // when & then
            val result = mockMvc.perform(delete("/contents")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            ).andExpect(MockMvcResultMatchers.status().isBadRequest)
                .andReturn()
                .response
                .getContentAsString(StandardCharsets.UTF_8)

            val actual = objectMapper.readValue(result, ExceptionResponse::class.java)
            Assertions.assertThat(actual.errorMessage).contains("url")
        }

        @Test
        @WithMockAuth
        fun `정상 정보를 입력하면 삭제에 성공한다`() {
            // given
            val requestBody = ContentDeleteRequest(
                url = "https://www.newspeed.store"
            )

            // when & then
            val result = mockMvc.perform(delete("/contents")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            ).andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()
                .response
                .getContentAsString(StandardCharsets.UTF_8)
        }
    }
}