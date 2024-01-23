package com.newspeed.domain.search.api.request

import com.newspeed.domain.content.config.YoutubeConfigProperties
import com.newspeed.domain.content.domain.enums.QueryOrder
import com.newspeed.domain.content.domain.enums.QueryPlatform
import com.newspeed.domain.content.event.ContentSearchEvent
import com.newspeed.domain.content.feign.enums.YoutubeQueryParts
import com.newspeed.domain.content.feign.request.YoutubeSearchRequest
import com.newspeed.domain.search.api.request.validation.ContentQueryConstraint
import com.newspeed.global.exception.content.UnavailableSaveQueryHistoryException
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime
import javax.validation.constraints.Past
import javax.validation.constraints.Positive

@ContentQueryConstraint
data class ContentSearchRequest(
    val query: String?,
    val platform: QueryPlatform,
    val order: QueryOrder = QueryOrder.views,

    @field:Past(message = "[publishedAfter] 해당 컨텐츠는 검색이 불가능합니다. 과거 시간을 기준으로 검색해주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val publishedAfter: LocalDateTime,

    @field:Positive(message = "[size] 0보다 큰 size를 입력해주세요.")
    val size: Int
) {
    fun toYoutubeSearchRequest(
        youtubeConfigProperties: YoutubeConfigProperties
    ): YoutubeSearchRequest = YoutubeSearchRequest(
        part = YoutubeQueryParts.snippet.name,
        key = youtubeConfigProperties.key,
        q = query,
        maxResults = size,
        order = order.toYoutubeSearchOrder()
//        publishedAfter = publishedAfter
    )

    fun toContentSearchEvent(
        userId: Long
    ): ContentSearchEvent = ContentSearchEvent(
        userId = userId,
        query = query ?: throw UnavailableSaveQueryHistoryException(),
        platform = platform
    )
}