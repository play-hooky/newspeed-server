package com.newspeed.domain.content.domain

import com.newspeed.domain.content.api.response.QueryHistoryResponse
import com.newspeed.domain.user.domain.User
import com.newspeed.global.model.BaseTimeEntity
import org.hibernate.annotations.Comment
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where
import javax.persistence.*

@Entity
@Table(name = "query_history")
@SQLDelete(sql = "UPDATE newspeed.query_history SET newspeed.query_history.deleted_at = now() WHERE id = ?")
@Where(clause = "deleted_at is null")
class QueryHistory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Comment("검색 기록 ID")
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Comment("사용자 ID")
    var user: User,

    @Comment("검색어")
    @Column(name = "query", length = 256, nullable = false)
    var query: String
): BaseTimeEntity() {
    fun toQueryHistoryDTO() = QueryHistoryResponse.QueryHistory(
        id = id,
        query = query
    )
}

fun List<QueryHistory>.toQueryHistoryResponse() = QueryHistoryResponse(
    queryHistories = this
        .map { it.toQueryHistoryDTO() }
)