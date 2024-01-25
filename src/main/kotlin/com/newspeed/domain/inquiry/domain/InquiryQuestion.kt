package com.newspeed.domain.inquiry.domain

import com.newspeed.domain.inquiry.api.response.InquiryResponse
import com.newspeed.domain.user.domain.User
import com.newspeed.global.model.BaseTimeEntity
import org.hibernate.annotations.Comment
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where
import javax.persistence.*

@Entity
@Table(name = "inquiry_question")
@SQLDelete(sql = "UPDATE newspeed.inquiry_question SET newspeed.inquiry_question.deleted_at = now() WHERE id = ?")
@Where(clause = "deleted_at is null")
class InquiryQuestion(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Comment("id")
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @Comment("사용자 ID")
    var user: User,

    @Column(name = "title", nullable = false, length = 256)
    @Comment("문의 내용 제목")
    var title: String,

    @Column(name = "body", nullable = false, columnDefinition = "text")
    @Comment("본문 내용")
    var body: String
): BaseTimeEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as InquiryQuestion

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.toInt()
    }

    fun toInquiryQuestionDTO(): InquiryResponse.InquiryDTO.InquiryQuestionDTO = InquiryResponse.InquiryDTO.InquiryQuestionDTO(
        title = title,
        body = body,
        createdAt = createdAt
    )
}