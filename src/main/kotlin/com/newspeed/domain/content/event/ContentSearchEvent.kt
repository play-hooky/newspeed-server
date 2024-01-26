package com.newspeed.domain.content.event

import com.newspeed.domain.category.domain.Category
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

    fun toCategory(
        user: User
    ): Category = Category(
        user = user,
        name = query,
        platform = platform
    )
}
