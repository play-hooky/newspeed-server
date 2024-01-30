package com.newspeed.domain.content.application

import com.newspeed.domain.content.InstagramConfigProperties
import com.newspeed.domain.content.domain.enums.QueryPlatform
import com.newspeed.domain.content.dto.ContentHostDTO
import com.newspeed.domain.content.dto.ContentResponseDTO
import com.newspeed.domain.content.dto.ContentYoutubeDTO
import com.newspeed.domain.content.feign.InstagramClient
import com.newspeed.domain.content.feign.response.InstagramMediaResponse
import com.newspeed.domain.search.api.request.ContentSearchRequest
import com.newspeed.global.exception.search.NotFoundQueryException
import org.springframework.stereotype.Service

@Service
class InstagramSearchService(
    private val instagramClient: InstagramClient,
    private val instagramConfigProperties: InstagramConfigProperties
): ContentSearchClient {
    override fun getQueryPlatform(): QueryPlatform = QueryPlatform.INSTAGRAM

    override fun search(
        request: ContentSearchRequest
    ): List<ContentResponseDTO> {
        val hashtagID = getHashTagID(request)

        return getMediaResponse(request, hashtagID)
            .toContentResponseDTOs()
    }

    private fun getHashTagID(
        request: ContentSearchRequest
    ): String {
        val query = request.query ?: throw NotFoundQueryException()
        val hashTagIDRequest = instagramConfigProperties.toHashtagIDRequest(query)

        return instagramClient.findHashTagID(hashTagIDRequest)
            .hashTagID()
    }

    private fun getMediaResponse(
        request: ContentSearchRequest,
        hashTagId: String
    ): InstagramMediaResponse = if (request.order.isDate()) getRecentMediaResponse(hashTagId, request.size)
    else getTopMediaResponse(hashTagId, request.size)

    private fun getTopMediaResponse(
        hashTagId: String,
        size: Int
    ): InstagramMediaResponse {
        val request = instagramConfigProperties.toMediaRequest(size)
        return instagramClient.findTopMedias(hashTagId, request)
    }

    private fun getRecentMediaResponse(
        hashTagId: String,
        size: Int
    ): InstagramMediaResponse {
        val request = instagramConfigProperties.toMediaRequest(size)
        return instagramClient.findRecentMedias(hashTagId, request)
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