package com.newspeed.domain.content.repository

import com.newspeed.domain.content.dto.RecommendQueryDTO
import java.time.LocalDate

interface QueryHistoryCustomRepository {
    fun findDailyMaxQueryHistory(date: LocalDate, size: Int): List<RecommendQueryDTO>
}