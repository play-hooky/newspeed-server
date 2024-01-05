package com.newspeed.domain.content.domain

import com.newspeed.domain.content.domain.enums.QueryPlatform
import com.newspeed.domain.user.domain.User
import com.newspeed.global.model.BaseTimeEntity
import org.hibernate.annotations.Comment
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where
import javax.persistence.*

@Entity
@Table(name = "content")
@SQLDelete(sql = "UPDATE newspeed.content SET newspeed.content.deleted_at = now() WHERE id = ?")
@Where(clause = "deleted_at is null")
class Content(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Comment("id")
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @Comment("사용자 ID")
    var user: User,

    @Comment("컨텐츠 url")
    @Column(name = "url", length = 512, nullable = false)
    var url: String,

    @Comment("SNS 플랫폼 INSTAGRAM, YOUTUBE")
    @Enumerated(EnumType.STRING)
    @Column(name = "platform", nullable = false, columnDefinition = "enum('YOUTUBE','INSTAGRAM','NEWSPEED')")
    var platform: QueryPlatform,
): BaseTimeEntity() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Content

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.toInt()
    }
}