package com.newspeed.domain.content.application

import com.newspeed.domain.content.domain.enums.QueryOrder
import com.newspeed.domain.content.domain.enums.QueryPlatform
import com.newspeed.domain.search.api.request.ContentSearchRequest
import com.newspeed.template.IntegrationTestTemplate
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDateTime

@DisplayName("Youtube 검색 서비스 계층 내")
@Disabled
class YoutubeSearchServiceIntegrationTest: IntegrationTestTemplate {

    @Autowired
    private lateinit var youtubeSearchService: YoutubeSearchService

    @Nested
    inner class `QueryPlatform을 조회하면` {

        @Test
        fun `유튜브를 반환한다`() {
            // given
            val expected = QueryPlatform.YOUTUBE

            // when
            val actual = youtubeSearchService.getQueryPlatform()

            Assertions.assertThat(actual).isEqualTo(expected)
        }
    }

    @Nested
    inner class `사용자 검색으로 동영상을 조회하면` {

        @Test
        fun `검색어로 유튜브 api에 요청하여 결과를 반환한다`() {
            // given
            val request = ContentSearchRequest(
                query = "축구",
                platform = QueryPlatform.YOUTUBE,
                order = QueryOrder.views,
                publishedAfter = LocalDateTime.now().minusYears(1),
                size = 5
            )

            // when
            val actual = youtubeSearchService.searchDetailBy(request)

            // then
            Assertions.assertThat(actual.size).isEqualTo(request.size)
            actual.forEach {
                Assertions.assertThat(it.platform).isEqualTo(QueryPlatform.YOUTUBE)
                Assertions.assertThat(it.host).isNotNull
                Assertions.assertThat(it.youtube).isNotNull
                Assertions.assertThat(it.instagram).isNull()
            }
        }
    }

    @Nested
    inner class `여러 id를 바탕으로 동영상을 조회하면` {

        @Test
        fun `id로 유튜브 api에 요청하여 결과를 반환한다`() {
            // given
            val ids = listOf("pM1y0hF4-Bk", "Cc1VHYGhtmc")

            // when
            val actual = youtubeSearchService.searchDetailBy(ids)

            // then
            Assertions.assertThat(actual.size).isEqualTo(ids.size)
            actual.forEach {
                Assertions.assertThat(it.platform).isEqualTo(QueryPlatform.YOUTUBE)
                Assertions.assertThat(it.host).isNotNull
                Assertions.assertThat(it.youtube).isNotNull
                Assertions.assertThat(it.instagram).isNull()
            }
        }
    }
}