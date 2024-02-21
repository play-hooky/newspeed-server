package com.newspeed.domain.content

import com.newspeed.domain.content.feign.enums.InstagramFields
import com.newspeed.domain.content.feign.request.InstagramHashTagIDRequest
import com.newspeed.domain.content.feign.request.InstagramMediaRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
data class InstagramConfigProperties(
    @Value("\${content.instagram.key}") val accessToken: String,
    @Value("\${content.instagram.userID}") val instagramUserID: String,
) {
    fun toHashtagIDRequest(
        query: String
    ): InstagramHashTagIDRequest = InstagramHashTagIDRequest(
        accessToken = accessToken,
        instagramUserId = instagramUserID,
        query = query.replace(" ", "")
    )

    fun toMediaRequest(
        limit: Int
    ): InstagramMediaRequest = InstagramMediaRequest(
        instagramUserId = instagramUserID,
        accessToken = accessToken,
        fields = InstagramFields.toString(),
        limit = limit
    )
}
