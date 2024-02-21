package com.newspeed.domain.content.application

import com.newspeed.domain.content.InstagramConfigProperties
import com.newspeed.domain.content.domain.enums.QueryOrder
import com.newspeed.domain.content.domain.enums.QueryPlatform
import com.newspeed.domain.content.dto.ContentHostDTO
import com.newspeed.domain.content.dto.ContentInstagramDTO
import com.newspeed.domain.content.dto.ContentResponseDTO
import com.newspeed.domain.content.dto.ContentYoutubeDTO
import com.newspeed.domain.content.feign.InstagramClient
import com.newspeed.domain.content.feign.request.InstagramHashTagIDRequest
import com.newspeed.domain.content.feign.request.InstagramMediaRequest
import com.newspeed.domain.content.feign.response.InstagramHashTagIDResponse
import com.newspeed.domain.content.feign.response.InstagramMediaResponse
import com.newspeed.domain.search.api.request.ContentSearchRequest
import com.newspeed.global.exception.search.NotFoundQueryException
import com.newspeed.template.UnitTestTemplate
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import java.time.LocalDateTime

@DisplayName("인스타그램 검색 서비스 계층 내")
class InstagramSearchServiceTest: UnitTestTemplate {

    @Mock
    private lateinit var instagramClient: InstagramClient

    @Mock
    private lateinit var instagramConfigProperties: InstagramConfigProperties

    @InjectMocks
    private lateinit var instagramSearchService: InstagramSearchService

    @Nested
    inner class `QueryPlatform을 조회하면` {

        @Test
        fun `인스타그램을 반환한다`() {
            // given
            val expected = QueryPlatform.INSTAGRAM

            // when
            val actual = instagramSearchService.getQueryPlatform()

            // then
            Assertions.assertThat(actual).isEqualTo(expected)
        }
    }

    @Nested
    inner class `인스타그램에 검색하면` {

        @Test
        fun `조회순 검색일 때 인스타그램 인기 검색 API로 검색 결과를 반환한다`() {
            // given
            val request = ContentSearchRequest(
                query = "newspeed",
                platform = QueryPlatform.INSTAGRAM,
                order = QueryOrder.views,
                publishedAfter = LocalDateTime.now().minusYears(1),
                size = 5
            )
            val body = "안녕하세요. 뉴스피드입니다.#뉴스피드#후키"
            val hashTagIDRequest = InstagramHashTagIDRequest(
                accessToken = "newspeed",
                instagramUserId = "newspeed",
                query = "newspeed"
            )
            val instagramHashTagIDResponse = InstagramHashTagIDResponse(
                data = listOf(InstagramHashTagIDResponse.Data(
                    id = "newspeedHashtagID"
                ))
            )
            val instagramMediaRequest = InstagramMediaRequest(
                instagramUserId = "newspeed",
                fields = "",
                accessToken = "newspeed",
                limit = request.size
            )
            val instagramMediaResponse = InstagramMediaResponse(
                data = listOf(InstagramMediaResponse.Data(
                    id = "newspeed-id",
                    caption = body,
                    commentsCount = 123,
                    likeCount = 500,
                    mediaUrl = "https://www.newspeed.store/newspeed.jpg",
                    mediaType = "",
                    permalink = "https://www.newspeed.store/newspeed.jpg",
                    timestamp = ""
                )),
                paging = InstagramMediaResponse.Paging(
                    cursors = InstagramMediaResponse.Paging.Cursors(
                        after = ""
                    ),
                    next = ""
                )
            )
            val expected = listOf(ContentResponseDTO(
                platform = QueryPlatform.INSTAGRAM,
                host = ContentHostDTO(
                    profileImgUrl = "https://www.newspeed.store/newspeed.jpg",
                    nickname = "안녕하세요. 뉴스피드입니다."
                ),
                youtube = null,
                instagram = ContentInstagramDTO(
                    id = "newspeed-id",
                    imgUrls = listOf("https://www.newspeed.store/newspeed.jpg"),
                    title = "안녕하세요. 뉴스피드입니다.",
                    body = "안녕하세요. 뉴스피드입니다.",
                    url = "https://www.newspeed.store/newspeed.jpg",
                    hashTags = listOf("뉴스피드", "후키")
                )
            ))

            given(instagramConfigProperties.toHashtagIDRequest("newspeed"))
                .willReturn(hashTagIDRequest)

            given(instagramClient.findHashTagID(hashTagIDRequest))
                .willReturn(instagramHashTagIDResponse)

            given(instagramConfigProperties.toMediaRequest(request.size))
                .willReturn(instagramMediaRequest)

            given(instagramClient.findTopMedias("newspeedHashtagID", instagramMediaRequest))
                .willReturn(instagramMediaResponse)

            // when
            val actual = instagramSearchService.searchDetailBy(request)

            // then
            assertThat(actual).usingRecursiveComparison().isEqualTo(expected)
        }

        @Test
        fun `최신순 검색일 때 인스타그램 최근 검색 API로 검색 결과를 반환한다`() {
            // given
            val request = ContentSearchRequest(
                query = "newspeed",
                platform = QueryPlatform.INSTAGRAM,
                order = QueryOrder.date,
                publishedAfter = LocalDateTime.now().minusYears(1),
                size = 5
            )
            val body = "안녕하세요. 뉴스피드입니다.#뉴스피드#후키"
            val hashTagIDRequest = InstagramHashTagIDRequest(
                accessToken = "newspeed",
                instagramUserId = "newspeed",
                query = "newspeed"
            )
            val instagramHashTagIDResponse = InstagramHashTagIDResponse(
                data = listOf(InstagramHashTagIDResponse.Data(
                    id = "newspeedHashtagID"
                ))
            )
            val instagramMediaRequest = InstagramMediaRequest(
                instagramUserId = "newspeed",
                fields = "",
                accessToken = "newspeed",
                limit = request.size
            )
            val instagramMediaResponse = InstagramMediaResponse(
                data = listOf(InstagramMediaResponse.Data(
                    id = "newspeed-id",
                    caption = body,
                    commentsCount = 123,
                    likeCount = 500,
                    mediaUrl = "https://www.newspeed.store/newspeed.jpg",
                    mediaType = "",
                    permalink = "https://www.newspeed.store/newspeed.jpg",
                    timestamp = ""
                )),
                paging = InstagramMediaResponse.Paging(
                    cursors = InstagramMediaResponse.Paging.Cursors(
                        after = ""
                    ),
                    next = ""
                )
            )
            val expected = listOf(ContentResponseDTO(
                platform = QueryPlatform.INSTAGRAM,
                host = ContentHostDTO(
                    profileImgUrl = "https://www.newspeed.store/newspeed.jpg",
                    nickname = "안녕하세요. 뉴스피드입니다."
                ),
                youtube = null,
                instagram = ContentInstagramDTO(
                    id = "newspeed-id",
                    imgUrls = listOf("https://www.newspeed.store/newspeed.jpg"),
                    title = "안녕하세요. 뉴스피드입니다.",
                    body = "안녕하세요. 뉴스피드입니다.",
                    url = "https://www.newspeed.store/newspeed.jpg",
                    hashTags = listOf("뉴스피드", "후키")
                )
            ))

            given(instagramConfigProperties.toHashtagIDRequest("newspeed"))
                .willReturn(hashTagIDRequest)

            given(instagramClient.findHashTagID(hashTagIDRequest))
                .willReturn(instagramHashTagIDResponse)

            given(instagramConfigProperties.toMediaRequest(request.size))
                .willReturn(instagramMediaRequest)

            given(instagramClient.findRecentMedias("newspeedHashtagID", instagramMediaRequest))
                .willReturn(instagramMediaResponse)

            // when
            val actual = instagramSearchService.searchDetailBy(request)

            // then
            assertThat(actual).usingRecursiveComparison().isEqualTo(expected)
        }

        @Test
        fun `검색어가 존재하지 않으면 에러를 던진다`() {
            // given
            val request = ContentSearchRequest(
                query = null,
                platform = QueryPlatform.INSTAGRAM,
                order = QueryOrder.views,
                publishedAfter = LocalDateTime.now().minusYears(1),
                size = 5
            )

            // when & then
            Assertions.assertThatThrownBy { instagramSearchService.searchDetailBy(request) }
                .isInstanceOf(NotFoundQueryException::class.java)
        }
    }

    @Nested
    inner class `여러 컨텐츠를 검색할 때` {

        @Test
        fun `여러 ID를 바탕으로 검색하여 결과를 반환한다`() {
            // given
            val expected = listOf(
                ContentResponseDTO(
                    platform = QueryPlatform.YOUTUBE,
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
            val ids = listOf(
                "newspeed",
                "hooky"
            )

            // when
            val actaul = instagramSearchService.searchDetailBy(ids)

            // then
            assertThat(actaul).usingRecursiveComparison().isEqualTo(expected)
        }
    }
}