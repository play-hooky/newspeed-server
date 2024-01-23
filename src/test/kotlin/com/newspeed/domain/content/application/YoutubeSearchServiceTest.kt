package com.newspeed.domain.content.application

import com.newspeed.domain.content.config.YoutubeConfigProperties
import com.newspeed.domain.content.domain.enums.QueryPlatform
import com.newspeed.domain.content.feign.YoutubeClient
import com.newspeed.factory.content.ContentFactory
import com.newspeed.template.UnitTestTemplate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito

@DisplayName("Youtube 서비스에서 ")
class YoutubeSearchServiceTest: UnitTestTemplate {
    private val youtubeClient: YoutubeClient = Mockito.mock(YoutubeClient::class.java)

    private val youtubeConfigProperties = YoutubeConfigProperties(
        key = "hookyhookyhookyhookyhookyhookyhookyhookyhookyhookyhookyhooky",
        videoUrl = "https://www.youtube.com/watch?v="
    )

    private val youtubeSearchService = YoutubeSearchService(
        youtubeClient = youtubeClient,
        youtubeConfigProperties = youtubeConfigProperties
    )

    @Nested
    inner class `SNS 플랫폼을 조회하면` {
        @Test
        fun `Youtube를 조회한다`() {
            // given
            val expected = QueryPlatform.YOUTUBE

            // when
            val actual = youtubeSearchService.getQueryPlatform()

            // then
            assertThat(expected).isEqualTo(actual)
        }
    }

    @Nested
    inner class `Youtube로 검색하면` {

        @Test
        fun `동영상 url, 채널, 섬네일 등의 정보를 제공한다`() {
            // given
            val request = ContentFactory.createYoutubeSearchRequest()
            val searchResponse = ContentFactory.createYoutubeSearchResponse()
            val channelResponse = ContentFactory.createChannelResponse()
            val videoDetailResponse = ContentFactory.createVideoDetailResponse()
            val expected = ContentFactory.createContentSearchResponse()

            given(youtubeClient.search(request.toYoutubeSearchRequest(youtubeConfigProperties)))
                .willReturn(searchResponse)

            given(youtubeClient.getChannel(searchResponse.toChannelRequest(youtubeConfigProperties)))
                .willReturn(channelResponse)

            given(youtubeClient.getDetailContent(searchResponse.toVideoDetailRequest(youtubeConfigProperties)))
                .willReturn(videoDetailResponse)

            // when
            val actual = youtubeSearchService.search(request)

            // then
            assertThat(actual).usingRecursiveComparison().isEqualTo(expected)
        }
    }
}