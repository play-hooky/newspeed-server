package com.newspeed.factory.category

import com.newspeed.domain.category.domain.Category
import com.newspeed.domain.content.domain.enums.QueryPlatform
import com.newspeed.domain.user.domain.User

class CategoryFactory {
    companion object {
        fun createCategory(
            user: User
        ) = Category(
            user = user,
            name = "hooky",
            platform = QueryPlatform.YOUTUBE
        )
    }
}