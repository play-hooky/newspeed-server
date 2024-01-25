package com.newspeed.domain.inquiry.domain

import com.newspeed.domain.inquiry.api.response.InquiryResponse
import com.newspeed.global.model.BaseTimeEntity
import org.hibernate.annotations.Comment
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where
import javax.persistence.*

@Entity
@Table(name = "inquiry_answer")
@SQLDelete(sql = "UPDATE newspeed.inquiry_answer SET newspeed.inquiry_answer.deleted_at = now() WHERE id = ?")
@Where(clause = "deleted_at is null")
class InquiryAnswer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Comment("id")
    val id: Long = 0,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false, foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @Comment("문의 내용 id")
    var question: InquiryQuestion,

    @Column(name = "body", nullable = false, columnDefinition = "text")
    @Comment("문의 답변")
    var body: String,

    @Column(name = "created_by", nullable = false, length = 64)
    @Comment("답변한 사람")
    var createdBy: String
): BaseTimeEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as InquiryAnswer

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.toInt()
    }

    fun toInquiryAnswerDTO(): InquiryResponse.InquiryDTO.InquiryAnswerDTO = InquiryResponse.InquiryDTO.InquiryAnswerDTO(
        body = body,
        createdAt = createdAt
    )
}