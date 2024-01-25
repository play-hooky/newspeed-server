package com.newspeed.domain.inquiry.repository

import com.newspeed.domain.inquiry.domain.QInquiryAnswer.inquiryAnswer
import com.newspeed.domain.inquiry.domain.QInquiryQuestion.inquiryQuestion
import com.newspeed.domain.inquiry.dto.InquiryResponseDTO
import com.newspeed.domain.inquiry.dto.QInquiryResponseDTO
import com.newspeed.domain.user.domain.QUser.user
import com.querydsl.jpa.impl.JPAQueryFactory

class InquiryCustomRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
): InquiryCustomRepository {
    override fun findInquiryByUserId(
        userId: Long
    ): List<InquiryResponseDTO> = jpaQueryFactory
        .select(
            QInquiryResponseDTO(
                inquiryQuestion,
                inquiryAnswer
            )
        )
        .from(inquiryQuestion)
        .join(user)
            .on(user.id.eq(inquiryQuestion.user.id))
        .leftJoin(inquiryAnswer)
            .on(inquiryAnswer.question.id.eq(inquiryQuestion.id).and(inquiryAnswer.deletedAt.isNull))
        .where(user.id.eq(userId))
        .fetch()
}