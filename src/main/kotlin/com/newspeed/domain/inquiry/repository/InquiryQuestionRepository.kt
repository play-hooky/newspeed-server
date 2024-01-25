package com.newspeed.domain.inquiry.repository

import com.newspeed.domain.inquiry.domain.InquiryQuestion
import org.springframework.data.jpa.repository.JpaRepository

interface InquiryQuestionRepository: JpaRepository<InquiryQuestion, Long>, InquiryCustomRepository {
}