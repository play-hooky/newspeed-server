package com.newspeed.domain.inquiry.repository

import com.newspeed.domain.inquiry.dto.InquiryResponseDTO

interface InquiryCustomRepository {
    fun findInquiryByUserId(userId: Long): List<InquiryResponseDTO>
}