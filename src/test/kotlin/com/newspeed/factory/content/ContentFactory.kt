package com.newspeed.factory.content

import com.newspeed.domain.content.api.request.ContentSearchRequest
import com.newspeed.domain.content.api.response.ContentSearchResponse
import com.newspeed.domain.content.api.response.QueryHistoryResponse
import com.newspeed.domain.content.domain.QueryHistory
import com.newspeed.domain.content.domain.enums.QueryOrder
import com.newspeed.domain.content.domain.enums.QueryPlatform
import com.newspeed.domain.content.feign.response.YoutubeChannelResponse
import com.newspeed.domain.content.feign.response.YoutubeSearchResponse
import com.newspeed.domain.content.feign.response.YoutubeVideoDetailResponse
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
            pageInfo = YoutubeSearchResponse.Page(
                totalResults = 20,
                resultsPerPage = 20
            ),
            items = listOf(
                YoutubeSearchResponse.Item(
                kind = "",
                etag = "",
                id = YoutubeSearchResponse.Item.Id(
                    kind = "",
                    videoId = "1"
                ),
                snippet = YoutubeSearchResponse.Item.Snippet(
                    publishedAt = LocalDateTime.now().minusDays(10),
                    channelId = "",
                    title = "hoooky",
                    description = "",
                    thumbnails = YoutubeSearchResponse.Item.Snippet.Thumbnails(
                        default = YoutubeSearchResponse.Item.Snippet.Thumbnails.Thumbnail(
                            url = "",
                            width = 64,
                            height = 64
                        ),
                        medium = YoutubeSearchResponse.Item.Snippet.Thumbnails.Thumbnail(
                            url = "",
                            width = 64,
                            height = 64
                        ),
                        high = YoutubeSearchResponse.Item.Snippet.Thumbnails.Thumbnail(
                            url = "https://www.newspeed.store/hooky.jpg",
                            width = 64,
                            height = 64
                        )
                    ),
                    channelTitle = "happy-hooky",
                    liveBroadcastContent = "",
                    publishTime = LocalDateTime.now().minusDays(10)
                )
            ))
        )

        fun createChannelResponse() = YoutubeChannelResponse(
            kind = "",
            etag = "",
            pageInfo = YoutubeSearchResponse.Page(
                totalResults = 20,
                resultsPerPage = 20
            ),
            items = listOf(
                YoutubeChannelResponse.Item(
                    kind = "",
                    etag = "",
                    id = "",
                    snippet = YoutubeChannelResponse.Item.Snippet(
                        title = "happy-hooky",
                        description = "happy-hooky",
                        publishedAt = LocalDateTime.now(),
                        thumbnails = YoutubeChannelResponse.Item.Snippet.Thumbnails(
                            default = YoutubeChannelResponse.Item.Snippet.Thumbnails.Thumbnail(
                                url = "",
                                width = 64,
                                height = 64
                            ),
                            medium = YoutubeChannelResponse.Item.Snippet.Thumbnails.Thumbnail(
                                url = "",
                                width = 64,
                                height = 64
                            ),
                            high = YoutubeChannelResponse.Item.Snippet.Thumbnails.Thumbnail(
                                url = "https://www.newspeed.store/happy-hooky",
                                width = 64,
                                height = 64
                            )
                        ),
                        localized = YoutubeChannelResponse.Item.Snippet.Localized(
                            title = "",
                            description = ""
                        )

                    )
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
                    statistics = YoutubeVideoDetailResponse.Item.Statistics(
                        viewCount = "20",
                        likeCount = "12",
                        favoriteCount = "2",
                        commentCount = "1"
                    )
                )
            )
        )

        fun createContentSearchResponse() = ContentSearchResponse(
            listOf(
                ContentSearchResponse.Content(
                    platform = QueryPlatform.YOUTUBE,
                    host = ContentSearchResponse.Content.Host(
                        profileImgUrl = "https://www.newspeed.store/happy-hooky",
                        nickname = "happy-hooky"
                    ),
                    youtube = ContentSearchResponse.Content.Youtube(
                        thumbnailUrl = "https://www.newspeed.store/hooky.jpg",
                        title = "hoooky",
                        url = "https://www.youtube.com/watch?v=1",
                        view = "20회",
                        todayBefore = "10일 전"
                    ),
                    instagram = null
            ))
        )

        fun createQueryHistory(
            user: User
        ) = QueryHistory(
            id = 1L,
            user = user,
            query = query,
            platform = QueryPlatform.NEWSPPED
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
    }
}