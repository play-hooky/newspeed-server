package com.newspeed.domain.queryhistory.api

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.newspeed.domain.queryhistory.api.response.QueryHistoryResponse
import com.newspeed.domain.queryhistory.api.response.RecommendQueryResponse
import com.newspeed.domain.queryhistory.application.QueryHistoryService
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.nio.charset.StandardCharsets
import java.time.LocalDate

@CustomWebMvcTestTemplate
@DisplayName("검색 결과 관련 API 내")
class QueryHistoryControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var queryHistoryService: QueryHistoryService

    @Nested
    inner class `검색 결과를 조회할 때` {

        @Test
        fun `비회원이면 에러를 던진다` () {
            // when & then
            mockMvc.perform(get("/contents/query/history")
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.status().isBadRequest)
        }

        @Test
        @WithMockAuth
        fun `회원이면 결과를 반환한다` () {
            // given
            val expected = QueryHistoryResponse(
                queryHistories = listOf(
                    QueryHistoryResponse.QueryHistory(
                    id = 1L,
                    query = "newspeed"
                ))
            )

            given(queryHistoryService.getQueryHistory(1L))
                .willReturn(expected)

            // when
            val result = mockMvc.perform(get("/contents/query/history")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            ).andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()
                .response
                .getContentAsString(StandardCharsets.UTF_8)

            // then
            val actual = objectMapper.readValue(result, object:TypeReference<List<QueryHistoryResponse.QueryHistory>>(){})
            assertThat(actual).usingRecursiveComparison().isEqualTo(expected.queryHistories)
        }
    }

    @Nested
    inner class `검색어를 추천할 때` {

        @Test
        fun `로그인 여부에 관계 없이 결과를 반환한다`() {
            // given
            val expected = RecommendQueryResponse(
                query = listOf("newspeed", "hooky")
            )

            given(queryHistoryService.recommendQuery(LocalDate.now(), 5))
                .willReturn(expected)

            // when
            val result = mockMvc.perform(get("/contents/query/recommend")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            ).andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()
                .response
                .getContentAsString(StandardCharsets.UTF_8)

            // then
            val actual = objectMapper.readValue(result, object:TypeReference<List<String>>(){})
            assertThat(actual).usingRecursiveComparison().isEqualTo(expected.query)
        }
    }

    @Nested
    inner class `검색 결과를 삭제할 때` {

        @Test
        fun `비회원이면 에러를 던진다` () {
            // when & then
            mockMvc.perform(delete("/contents/query/history/1")
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.status().isBadRequest)
        }

        @Test
        @WithMockAuth
        fun `검색 결과 ID를 입력하지 않으면 에러를 던진다`() {
            // when & then
            mockMvc.perform(delete("/contents/query/history/")
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.status().is4xxClientError)
        }

        @Test
        @WithMockAuth
        fun `유효한 정보를 입력하면 삭제에 성공한다`() {
            mockMvc.perform(delete("/contents/query/history/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            ).andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()
                .response
                .getContentAsString(StandardCharsets.UTF_8)
        }
    }
}