package com.newspeed.domain.inquiry.api

import com.newspeed.domain.inquiry.api.request.InquiryQuestionRequest
import com.newspeed.domain.inquiry.api.response.InquiryResponse
import com.newspeed.domain.inquiry.application.InquiryService
import com.newspeed.domain.jwt.annotation.User
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/user/inquiry")
class InquiryController(
    private val inquiryService: InquiryService
) {

    @PostMapping
    fun saveInquiryQuestion(
        @User userId: Long,
        @Valid @RequestBody request: InquiryQuestionRequest
    ): ResponseEntity<Unit> {
        inquiryService.saveInquiryQuestion(
            request.toCommand(userId)
        )

        return ResponseEntity(HttpStatus.CREATED)
    }

    @GetMapping
    fun getInquiry(
        @User userId: Long
    ): ResponseEntity<InquiryResponse> = ResponseEntity.ok(
        inquiryService.getInquiry(userId)
    )
}