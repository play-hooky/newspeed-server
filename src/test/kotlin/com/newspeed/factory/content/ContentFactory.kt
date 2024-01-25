package com.newspeed.factory.content

import com.newspeed.domain.content.api.response.ContentsResponse
import com.newspeed.domain.content.application.command.ContentSaveCommand
import com.newspeed.domain.content.domain.Content
import com.newspeed.domain.content.domain.QueryHistory
import com.newspeed.domain.content.domain.enums.QueryOrder
import com.newspeed.domain.content.domain.enums.QueryPlatform
import com.newspeed.domain.content.dto.ContentHostDTO
import com.newspeed.domain.content.dto.ContentResponseDTO
import com.newspeed.domain.content.dto.ContentYoutubeDTO
import com.newspeed.domain.content.dto.RecommendQueryDTO
import com.newspeed.domain.content.feign.dto.*
import com.newspeed.domain.content.feign.response.YoutubeChannelResponse
import com.newspeed.domain.content.feign.response.YoutubeSearchResponse
import com.newspeed.domain.content.feign.response.YoutubeVideoDetailResponse
import com.newspeed.domain.queryhistory.api.response.QueryHistoryResponse
import com.newspeed.domain.search.api.request.ContentSearchRequest
import com.newspeed.domain.user.domain.User
import java.time.LocalDateTime

class ContentFactory {
    companion object {
        const val query = "hooky"


        fun createYoutubeSearchRequest() = ContentSearchRequest(
            query = query,
            platform = QueryPlatform.YOUTUBE,
            order = QueryOrder.views,
            publishedAfter = LocalDateTime.now().minusDays(10),
            size = 20
        )

        fun createYoutubeSearchResponse() = YoutubeSearchResponse(
            kind = "",
            etag = "",
            nextPageToken = "",
            regionCode = "KR",
            pageInfo = YoutubePageDTO(
                totalResults = 20,
                resultsPerPage = 20
            ),
            items = listOf(
                YoutubeSearchResponse.Item(
                kind = "",
                etag = "",
                id = YoutubeIDDTO(
                    kind = "",
                    videoId = "1"
                ),
                snippet = createYoutubeVideoSnippetDTO()
            ))
        )

        fun createChannelResponse() = YoutubeChannelResponse(
            kind = "",
            etag = "",
            pageInfo = YoutubePageDTO(
                totalResults = 20,
                resultsPerPage = 20
            ),
            items = listOf(
                YoutubeChannelResponse.Item(
                    kind = "",
                    etag = "",
                    id = "",
                    snippet = createYoutubeChannelSnippetDTO()
                )
            )
        )

        fun createVideoDetailResponse() = YoutubeVideoDetailResponse(
            kind = "",
            etag = "",
            items = listOf(
                YoutubeVideoDetailResponse.Item(
                    kind = "",
                    etag = "",
                    id = "1",
                    statistics = YoutubeVideoStatisticsDTO(
                        viewCount = "20",
                        likeCount = "12",
                        favoriteCount = "2",
                        commentCount = "1"
                    ),
                    snippet = createYoutubeVideoSnippetDTO()
                )
            )
        )

        fun createQueryHistory(
            user: User
        ) = QueryHistory(
            id = 1L,
            user = user,
            query = query,
            platform = QueryPlatform.NEWSPEED
        )

        fun createQueryHistories(
            user: User
        ) = listOf(createQueryHistory(user))

        fun createQueryHistoryResponse() = QueryHistoryResponse(
            queryHistories = listOf(
                QueryHistoryResponse.QueryHistory(
                    id = 1L,
                    query = query
                )
            )
        )

        fun createRecommendQueryDTOs(
            size: Int
        ): List<RecommendQueryDTO> = (1..size)
            .map { RecommendQueryDTO(
                query = "play-hooky${it}",
                count = it.toLong()
            ) }

        fun createContentSaveCommand(
            userId: Long
        ) = ContentSaveCommand(
            userId = userId,
            contentIdInPlatform = "1",
            url = "https://www.youtube.com"
        )

        fun createContentResponseDTOs() = listOf(
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

        fun createContents(
            user: User
        ) = listOf(
            Content(
                user = user,
                contentIdInPlatform = "1",
                url = "https://www.youtube.com/watch?v=1",
                platform = QueryPlatform.NEWSPEED
            )
        )

        fun createYoutubeChannelSnippetDTO() = YoutubeChannelSnippetDTO(
            publishedAt = LocalDateTime.now().minusDays(10),
            title = "hoooky",
            description = "",
            thumbnails = YoutubeThumbnailDTO(
                default = YoutubeThumbnailDTO.Thumbnail(
                    url = "",
                    width = 64,
                    height = 64
                ),
                medium = YoutubeThumbnailDTO.Thumbnail(
                    url = "",
                    width = 64,
                    height = 64
                ),
                high = YoutubeThumbnailDTO.Thumbnail(
                    url = "https://www.newspeed.store/happy-hooky",
                    width = 64,
                    height = 64
                )
            ),
            localized = YoutubeChannelSnippetDTO.Localized(
                title = "",
                description = ""
            )
        )

        private fun createYoutubeVideoSnippetDTO() = YoutubeVideoSnippetDTO(
            publishedAt = LocalDateTime.now().minusDays(10),
            channelId = "",
            title = "hoooky",
            description = "",
            thumbnails = YoutubeThumbnailDTO(
                default = YoutubeThumbnailDTO.Thumbnail(
                    url = "",
                    width = 64,
                    height = 64
                ),
                medium = YoutubeThumbnailDTO.Thumbnail(
                    url = "",
                    width = 64,
                    height = 64
                ),
                high = YoutubeThumbnailDTO.Thumbnail(
                    url = "https://www.newspeed.store/hooky.jpg",
                    width = 64,
                    height = 64
                )
            ),
            channelTitle = "happy-hooky",
            liveBroadcastContent = ""
        )

        fun createContentsResponse() = ContentsResponse(
            contentResponses = createContentResponseDTOs()
        )
    }
}