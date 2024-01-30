package com.newspeed.domain.content.feign.response

import com.newspeed.global.exception.search.NotFoundQueryException

data class InstagramHashTagIDResponse(
    val data: List<Data>
) {
    data class Data(
        val id: String
    )

    fun hashTagID(): String = this
        .data
        .firstOrNull()
        ?.id
        ?: throw NotFoundQueryException()
}
