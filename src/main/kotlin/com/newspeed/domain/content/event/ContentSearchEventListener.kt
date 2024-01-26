package com.newspeed.domain.content.event

import com.newspeed.domain.category.repository.CategoryRepository
import com.newspeed.domain.content.repository.QueryHistoryRepository
import com.newspeed.domain.user.application.UserService
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class ContentSearchEventListener(
    private val queryHistoryRepository: QueryHistoryRepository,
    private val categoryRepository: CategoryRepository,
    private val userService: UserService
) {

    @EventListener
    @Async(value = "asyncTaskExecutor")
    fun handleContentSearchEvent(event: ContentSearchEvent) {
        val user = userService.getUser(event.userId)

        queryHistoryRepository.save(event.toQueryHistory(user))
        categoryRepository.save(event.toCategory(user))
    }
}