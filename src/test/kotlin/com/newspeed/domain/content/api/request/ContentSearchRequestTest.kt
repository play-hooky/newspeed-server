package com.newspeed.domain.content.api.request

import com.newspeed.domain.content.domain.enums.QueryOrder
import com.newspeed.domain.content.domain.enums.QueryPlatform
import com.newspeed.template.UnitTestTemplate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import javax.validation.Validation
import javax.validation.Validator
import javax.validation.ValidatorFactory

@DisplayName("검색 API request param 객체는 ")
class ContentSearchRequestTest: UnitTestTemplate {
    companion object {
        private val factory: ValidatorFactory = Validation.buildDefaultValidatorFactory()
        private val validator: Validator = factory.validator

        @JvmStatic
        @AfterAll
        fun close() {
            factory.close()
        }
    }

    @Test
    fun `미래를 기준으로 생성되지 않은 컨텐츠를 검색하려하면 에러를 던진다`() {
        // given
        val request = ContentSearchRequest(
            query = null,
            platform = QueryPlatform.YOUTUBE,
            order = QueryOrder.date,
            publishedAfter = LocalDateTime.now().plusDays(1),
            size = 1
        )

        // when
        val result = validator.validate(request)

        // then
        assertThat(result.joinToString { it.messageTemplate }).contains("publishedAfter")
    }

    @Test
    fun `검색 size를 0 이하로 요청하면 에러를 던진다`() {
        // given
        val request = ContentSearchRequest(
            query = null,
            platform = QueryPlatform.YOUTUBE,
            order = QueryOrder.date,
            publishedAfter = LocalDateTime.now().minusDays(1),
            size = 0
        )

        // when
        val result = validator.validate(request)

        // then
        assertThat(result.joinToString { it.messageTemplate }).contains("size")
    }

    @Test
    fun `검색어 없이 인기순으로 검색하면 에러를 던진다`() {
        // given
        val request = ContentSearchRequest(
            query = null,
            platform = QueryPlatform.YOUTUBE,
            order = QueryOrder.views,
            publishedAfter = LocalDateTime.now().minusDays(1),
            size = 1
        )

        // when
        val result = validator.validate(request)

        // then
        assertThat(result.joinToString { it.messageTemplate }).contains("order")
    }

    @Test
    fun `정상 입력값으로 검색하면 성공한다`() {
        // given
        val request = ContentSearchRequest(
            query = "인스타그램",
            platform = QueryPlatform.YOUTUBE,
            order = QueryOrder.views,
            publishedAfter = LocalDateTime.now().minusDays(1),
            size = 1
        )

        // when
        val result = validator.validate(request)

        // then
        assertThat(result).isEmpty()
    }
}