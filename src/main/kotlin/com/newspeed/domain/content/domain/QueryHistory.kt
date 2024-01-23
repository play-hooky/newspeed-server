package com.newspeed.domain.content.domain

import com.newspeed.domain.content.domain.enums.QueryPlatform
import com.newspeed.domain.queryhistory.api.response.QueryHistoryResponse
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
    @JoinColumn(name = "user_id", nullable = false, foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @Comment("사용자 ID")
    var user: User,

    @Comment("검색어")
    @Column(name = "query", length = 256, nullable = false)
    var query: String,

    @Comment("SNS 플랫폼 INSTAGRAM, YOUTUBE")
    @Enumerated(EnumType.STRING)
    @Column(name = "platform", nullable = false, columnDefinition = "enum('INSTAGRAM','YOUTUBE','NEWSPEED')")
    var platform: QueryPlatform,
): BaseTimeEntity() {
    fun toQueryHistoryDTO() = QueryHistoryResponse.QueryHistory(
        id = id,
        query = query
    )

    fun isCreatedBy(
        thatUser: User
    ): Boolean = this.user == thatUser

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QueryHistory

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.toInt()
    }
}

fun List<QueryHistory>.toQueryHistoryResponse() = QueryHistoryResponse(
    queryHistories = this
        .map { it.toQueryHistoryDTO() }
)