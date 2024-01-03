package com.newspeed.domain.content.event

import com.newspeed.domain.content.domain.QueryHistory
import com.newspeed.domain.content.domain.enums.QueryPlatform
import com.newspeed.domain.user.domain.User

data class ContentSearchEvent(
    val userId: Long,
    val query: String,
    val platform: QueryPlatform
) {
    fun toQueryHistory(
        user: User
    ): QueryHistory = QueryHistory(
        user = user,
        query = query,
        platform = platform
    )
}
