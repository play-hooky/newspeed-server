package com.newspeed.domain.search.application

import com.newspeed.domain.content.domain.enums.QueryOrder
import com.newspeed.domain.content.domain.enums.QueryPlatform
import com.newspeed.domain.content.dto.ContentHostDTO
import com.newspeed.domain.content.dto.ContentResponseDTO
import com.newspeed.domain.content.dto.ContentYoutubeDTO
import com.newspeed.domain.content.event.ContentSearchEvent
import com.newspeed.domain.search.api.request.ContentSearchRequest
import com.newspeed.domain.search.api.response.ContentSearchResponse
import com.newspeed.template.IntegrationTestTemplate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.event.ApplicationEvents
import org.springframework.test.context.event.RecordApplicationEvents
import java.time.LocalDateTime

@DisplayName("SearchService 내")
@RecordApplicationEvents
class SearchServiceIntegrationTest: IntegrationTestTemplate {

    @Autowired
    private lateinit var searchService: SearchService

    @Autowired
    private lateinit var applicationEvents: ApplicationEvents

    @Nested
    inner class `검색 API가 사용하는 search 메서드는` {
        @Test
        fun `해당 검색 플랫폼에 질의하여 결과를 전달한다`() {
            // given
            val request = ContentSearchRequest(
                query = "hooky",
                platform = QueryPlatform.NEWSPEED,
                order = QueryOrder.date,
                publishedAfter = LocalDateTime.now().minusMinutes(6),
                size = 1
            )
            val expected = ContentSearchResponse(
                contents = listOf(ContentResponseDTO(
                    platform = QueryPlatform.NEWSPEED,
                    host = ContentHostDTO(
                        profileImgUrl = "https://www.newspeed.store/happy-hooky",
                        nickname = "happy-hooky"
                    ),
                    youtube = ContentYoutubeDTO(
                        id = "1",
                        thumbnailUrl = "https://www.newspeed.store/hooky.jpg",
                        title = "hoooky",
                        url = "https://www.youtube.com/watch?v=1",
                        views = "20회",
                        todayBefore = "10일 전"
                    ),
                    instagram = null
                ))
            )

            // when
            val actual = searchService.search(request)

            // then
            assertThat(actual).usingRecursiveComparison().isEqualTo(expected)
        }
    }

    @Nested
    inner class `미디어 id로 검색하는 search 메서드는` {

        @Test
        fun `여러 미디어를 검색하여 결과를 전달한다`() {
            // given
            val expected = listOf(
                ContentResponseDTO(
                    platform = QueryPlatform.NEWSPEED,
                    host = ContentHostDTO(
                        profileImgUrl = "https://www.newspeed.store/happy-hooky",
                        nickname = "happy-hooky"
                    ),
                    youtube = ContentYoutubeDTO(
                        id = "1",
                        thumbnailUrl = "https://www.newspeed.store/hooky.jpg",
                        title = "hoooky",
                        url = "https://www.youtube.com/watch?v=1",
                        views = "20회",
                        todayBefore = "10일 전"
                    ),
                    instagram = null
                )
            )

            // when
            val actual = searchService.search(QueryPlatform.NEWSPEED, listOf("1"))

            // then
            assertThat(actual).usingRecursiveComparison().isEqualTo(expected)
        }
    }

    @Nested
    inner class `로그인한 사용자가 검색하는 search 메서드는` {

        @Test
        fun `검색 기록을 저장하고 검색 결과를 반환한다`() {
            // given
            val userId = 1L
            val platform = QueryPlatform.NEWSPEED
            val query = "hooky"
            val request = ContentSearchRequest(
                query = query,
                platform = platform,
                order = QueryOrder.date,
                publishedAfter = LocalDateTime.now().minusMinutes(6),
                size = 1
            )
            val expected = ContentSearchResponse(
                contents = listOf(ContentResponseDTO(
                    platform = platform,
                    host = ContentHostDTO(
                        profileImgUrl = "https://www.newspeed.store/happy-hooky",
                        nickname = "happy-hooky"
                    ),
                    youtube = ContentYoutubeDTO(
                        id = "1",
                        thumbnailUrl = "https://www.newspeed.store/hooky.jpg",
                        title = "hoooky",
                        url = "https://www.youtube.com/watch?v=1",
                        views = "20회",
                        todayBefore = "10일 전"
                    ),
                    instagram = null
                ))
            )

            // when
            val actual = searchService.search(userId, request)

            // then
            assertThat(actual).usingRecursiveComparison().isEqualTo(expected)
            val eventCount = applicationEvents
                .stream(ContentSearchEvent::class.java)
                .count()

            assertThat(eventCount).isEqualTo(1)
        }
    }
}