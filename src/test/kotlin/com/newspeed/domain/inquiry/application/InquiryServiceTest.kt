package com.newspeed.domain.inquiry.application

import com.newspeed.domain.inquiry.application.command.InquiryQuestionCommand
import com.newspeed.domain.inquiry.domain.InquiryQuestion
import com.newspeed.domain.inquiry.repository.InquiryQuestionRepository
import com.newspeed.domain.user.application.UserService
import com.newspeed.factory.auth.AuthFactory.Companion.createKakaoUser
import com.newspeed.template.UnitTestTemplate
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito

@DisplayName("문의 내용 서비스 계층에서 ")
class InquiryServiceTest: UnitTestTemplate {

    @Mock
    private lateinit var inquiryQuestionRepository: InquiryQuestionRepository

    @Mock
    private lateinit var userService: UserService

    @InjectMocks
    private lateinit var inquiryService: InquiryService

    @Nested
    inner class `문의 내용을 등록할 때` {

        @Test
        fun `사용자를 조회하여 문의내용을 DB에 저장한다`() {
            // given
            val user = createKakaoUser()
            val command = InquiryQuestionCommand(
                userId = user.id,
                title = "hooky",
                body = "happy hooky"
            )
            val inquiryQuestion = InquiryQuestion(
                user = user,
                title = command.title,
                body = command.body
            )

            given(userService.getUser(user.id))
                .willReturn(user)

            // when
            inquiryService.saveInquiryQuestion(command)

            // then
            Mockito.verify(inquiryQuestionRepository, times(1)).save(inquiryQuestion)
        }
    }
}