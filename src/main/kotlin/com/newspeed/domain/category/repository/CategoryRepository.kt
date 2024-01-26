package com.newspeed.domain.category.repository

import com.newspeed.domain.category.domain.Category
import com.newspeed.domain.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository: JpaRepository<Category, Long> {
    fun findByUser(user: User): List<Category>
}