package com.newspeed.domain.queryhistory.application

import com.newspeed.domain.content.domain.toQueryHistoryResponse
import com.newspeed.domain.content.dto.toRecommendQueryResponse
import com.newspeed.domain.content.repository.QueryHistoryRepository
import com.newspeed.domain.queryhistory.api.response.QueryHistoryResponse
import com.newspeed.domain.queryhistory.api.response.RecommendQueryResponse
import com.newspeed.domain.user.application.UserService
import com.newspeed.global.exception.content.NotFoundQueryHistoryException
import com.newspeed.global.exception.content.UnavailableDeleteQueryHistoryException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
@Transactional
class QueryHistoryService(
    private val userService: UserService,
    private val queryHistoryRepository: QueryHistoryRepository
) {

    @Transactional(readOnly = true)
    fun getQueryHistory(
        userId: Long
    ): QueryHistoryResponse {
        val user = userService.getUser(userId)

        return queryHistoryRepository.findByUser(user)
            .toQueryHistoryResponse()
    }

    @Transactional(readOnly = true)
    fun recommendQuery(
        date: LocalDate,
        size: Int
    ): RecommendQueryResponse = queryHistoryRepository.findDailyMaxQueryHistory(date, size)
        .toRecommendQueryResponse()

    fun deleteQueryHistory(
        userId: Long,
        queryHistoryId: Long
    ) {
        val user = userService.getUser(userId)
        val queryHistory = queryHistoryRepository.findByIdOrNull(queryHistoryId)
            ?: throw NotFoundQueryHistoryException()

        queryHistory.takeIf { it.isCreatedBy(user) }
            ?.delete()
            ?: throw UnavailableDeleteQueryHistoryException()
    }
}