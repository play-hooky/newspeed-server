package com.newspeed.domain.content.repository

import com.newspeed.domain.content.domain.QQueryHistory.queryHistory
import com.newspeed.domain.content.dto.RecommendQueryDTO
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import java.time.LocalDate

class QueryHistoryCustomRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
): QueryHistoryCustomRepository {
    override fun findDailyMaxQueryHistory(
        date: LocalDate,
        size: Int
    ): List<RecommendQueryDTO> = jpaQueryFactory
            .select(
                Projections.fields(
                    RecommendQueryDTO::class.java,
                    queryHistory.query,
                    queryHistory.id.count().`as`("count")
                )
            )
            .from(queryHistory)
            .where(queryHistory.createdAt.between(date.atStartOfDay(), date.plusDays(1).atStartOfDay()))
            .groupBy(queryHistory.query)
            .orderBy(queryHistory.id.count().desc())
            .limit(size.toLong())
            .fetch()
}