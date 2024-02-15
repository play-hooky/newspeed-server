package com.newspeed.domain.category.api

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.newspeed.domain.category.api.response.CategoryResponse
import com.newspeed.domain.category.application.CategoryService
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.nio.charset.StandardCharsets

@CustomWebMvcTestTemplate
@DisplayName("Category 컨트롤러 내")
class CategoryControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var categoryService: CategoryService

    @Nested
    inner class `카테고리 조회 API에서` {

        @Test
        fun `비회원이면 에러를 던진다`() {
            // when & then
            mockMvc.perform(get("/contents/categories")
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.status().isBadRequest)
        }

        @Test
        @WithMockAuth
        fun `회원이면 카테고리 조회에 성공한다`() {
            // given
            val expected = CategoryResponse(
                categories = listOf(
                    CategoryResponse.CategoryResponseDTO(
                    id = 1L,
                    category = "newspeed"
                ))
            )

            given(categoryService.getCategories(1L))
                .willReturn(expected)

            // when
            val result = mockMvc.perform(get("/contents/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            ).andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()
                .response
                .getContentAsString(StandardCharsets.UTF_8)

            // then
            val actual: List<CategoryResponse.CategoryResponseDTO> = objectMapper.readValue(result, object:TypeReference<List<CategoryResponse.CategoryResponseDTO>>(){})
            Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected.categories)
        }
    }

    @Nested
    inner class `카테고리 삭제 API에서` {

        @Test
        fun `비회원이면 에러를 던진다`() {
            // when & then
            mockMvc.perform(delete("/contents/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.status().isBadRequest)
        }

        @Test
        @WithMockAuth
        fun `카테고리 ID를 입력하지 않으면 에러를 던진다`() {
            // when & then
            mockMvc.perform(delete("/contents/categories/")
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.status().is4xxClientError)
        }

        @Test
        @WithMockAuth
        fun `회원이면 카테고리 삭제에 성공한다`() {
            // when
            mockMvc.perform(delete("/contents/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
            ).andExpect(MockMvcResultMatchers.status().isOk)
        }
    }
}