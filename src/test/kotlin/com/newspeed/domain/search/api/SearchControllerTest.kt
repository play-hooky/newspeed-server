package com.newspeed.domain.search.api

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.newspeed.domain.content.domain.enums.QueryOrder
import com.newspeed.domain.content.domain.enums.QueryPlatform
import com.newspeed.domain.search.api.request.ContentSearchRequest
import com.newspeed.domain.search.application.SearchService
import com.newspeed.template.CustomWebMvcTestTemplate
import com.newspeed.util.WithMockAuth
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.anyLong
import org.mockito.Mockito.times
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@CustomWebMvcTestTemplate
@DisplayName("검색 관련 API 내")
class SearchControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var searchService: SearchService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Nested
    inner class `검색 API에서` {

        @Test
        fun `비회원이면 검색만 수행한다`() {
            // given
            val requestBody = ContentSearchRequest(
                query = "newspeed",
                platform = QueryPlatform.YOUTUBE,
                order = QueryOrder.views,
                publishedAfter = LocalDateTime.now().minusYears(1).withNano(0),
                size = 5
            )

            // when
            mockMvc.perform(get("/contents/search")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                .queryParams(convertToQueryParams(requestBody))
            ).andExpect(status().isOk)

            Mockito.verify(searchService, times(1)).search(requestBody)
        }

        @Test
        @WithMockAuth
        fun `회원이면 검색 이후 이벤트도 수행한다`() {
            // given
            val requestBody = ContentSearchRequest(
                query = "newspeed",
                platform = QueryPlatform.YOUTUBE,
                order = QueryOrder.views,
                publishedAfter = LocalDateTime.now().minusYears(1).withNano(0),
                size = 5
            )

            // when
            mockMvc.perform(get("/contents/search")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                .queryParams(convertToQueryParams(requestBody))
            ).andExpect(status().isOk)

            Mockito.verify(searchService, times(1)).search(1L, requestBody)
        }

        @Test
        @WithMockAuth
        fun `검색어 없이 최신순으로 검색하면 에러를 던진다`() {
            // given
            val requestBody = ContentSearchRequest(
                query = null,
                platform = QueryPlatform.YOUTUBE,
                order = QueryOrder.views,
                publishedAfter = LocalDateTime.now().minusYears(1).withNano(0),
                size = 5
            )

            // when
            mockMvc.perform(get("/contents/search")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                .queryParams(convertToQueryParams(requestBody))
            ).andExpect(status().isBadRequest)
        }

        @Test
        @WithMockAuth
        fun `유효하지 않은 검색 기준 시간을 입력하면 에러를 던진다`() {
            // given
            val requestBody = ContentSearchRequest(
                query = "newspeed",
                platform = QueryPlatform.YOUTUBE,
                order = QueryOrder.views,
                publishedAfter = LocalDateTime.now().plusYears(1).withNano(0),
                size = 5
            )

            // when
            mockMvc.perform(get("/contents/search")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                .queryParams(convertToQueryParams(requestBody))
            ).andExpect(status().isBadRequest)
        }

        @Test
        @WithMockAuth
        fun `유효하지 않은 검색 개수를 입력하면 에러를 던진다`() {
            // given
            val requestBody = ContentSearchRequest(
                query = "newspeed",
                platform = QueryPlatform.YOUTUBE,
                order = QueryOrder.views,
                publishedAfter = LocalDateTime.now().minusYears(1).withNano(0),
                size = -1
            )

            // when
            mockMvc.perform(get("/contents/search")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                .queryParams(convertToQueryParams(requestBody))
            ).andExpect(status().isBadRequest)
        }

        private fun convertToQueryParams(
            requestBody: ContentSearchRequest
        ): MultiValueMap<String, String> {
            val params = LinkedMultiValueMap<String, String>()
            val map = objectMapper.convertValue(requestBody, object : TypeReference<Map<String?, String?>?>() {})
            params.setAll(map)
            params["publishedAfter"] = listOf(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(requestBody.publishedAfter))

            return params
        }
    }
}