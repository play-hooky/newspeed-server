package com.newspeed.domain.content.application

import com.newspeed.domain.content.domain.enums.QueryPlatform
import com.newspeed.domain.content.dto.ContentHostDTO
import com.newspeed.domain.content.dto.ContentResponseDTO
import com.newspeed.domain.content.dto.ContentYoutubeDTO
import com.newspeed.domain.search.api.request.ContentSearchRequest
import com.newspeed.domain.search.api.response.ContentSearchResponse
import org.springframework.stereotype.Service

@Service
class InstagramSearchService: ContentSearchClient {
    override fun getQueryPlatform(): QueryPlatform = QueryPlatform.INSTAGRAM

    override fun search(
        request: ContentSearchRequest
    ): ContentSearchResponse {
        TODO("Not yet implemented")
    }

    override fun search(ids: List<String>): List<ContentResponseDTO> {
        // TODO

        return listOf(
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
    }
}