package com.newspeed.domain.category.domain

import com.newspeed.domain.category.api.response.CategoryResponse
import com.newspeed.domain.content.domain.enums.QueryPlatform
import com.newspeed.domain.user.domain.User
import com.newspeed.global.model.BaseTimeEntity
import org.hibernate.annotations.Comment
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where
import javax.persistence.*

@Entity
@Table(name = "category")
@SQLDelete(sql = "UPDATE newspeed.category SET newspeed.category.deleted_at = now() WHERE id = ?")
@Where(clause = "deleted_at is null")
class Category(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Comment("검색 기록 ID")
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @Comment("사용자 ID")
    var user: User,

    @Comment("카테고리")
    @Column(name = "name", length = 256, nullable = false)
    var name: String,

    @Comment("SNS 플랫폼 INSTAGRAM, YOUTUBE")
    @Enumerated(EnumType.STRING)
    @Column(name = "platform", nullable = false, columnDefinition = "enum('INSTAGRAM','YOUTUBE','NEWSPEED')")
    var platform: QueryPlatform,
): BaseTimeEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Category

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.toInt()
    }

    fun toCategoryResponseDTO(): CategoryResponse.CategoryResponseDTO = CategoryResponse.CategoryResponseDTO(
        id = id,
        category = name
    )
}