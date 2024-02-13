package com.newspeed.domain.inquiry.application

import com.newspeed.domain.inquiry.application.command.InquiryQuestionCommand
import com.newspeed.domain.inquiry.domain.InquiryQuestion
import com.newspeed.domain.inquiry.repository.InquiryQuestionRepository
import com.newspeed.domain.user.domain.User
import com.newspeed.domain.user.repository.UserRepository
import com.newspeed.factory.auth.AuthFactory.Companion.createKakaoUser
import com.newspeed.template.IntegrationTestTemplate
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@DisplayName("InquiryService 계층 내")
class InquiryServiceIntegrationTest: IntegrationTestTemplate {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var inquiryRepository: InquiryQuestionRepository

    @Autowired
    private lateinit var inquiryService: InquiryService

    private lateinit var user: User

    @BeforeEach
    fun setup() {
        user = userRepository.save(
            createKakaoUser()
        )
    }

    @Nested
    inner class `문의 내역을 저장할 때` {

        @Test
        fun `사용자가 입력한 내용을 DB에 저장한다`() {
            // given
            val command = InquiryQuestionCommand(
                userId = user.id,
                title = "안녕하세요",
                body = "잘부탁드립니다"
            )

            // when
            inquiryService.saveInquiryQuestion(command)

            // then
            val inquiries = inquiryRepository.findAll()
            SoftAssertions.assertSoftly { softly: SoftAssertions ->
                softly.assertThat(inquiries[0].user.id).isEqualTo(command.userId)
                softly.assertThat(inquiries[0].body).isEqualTo(command.body)
                softly.assertThat(inquiries[0].title).isEqualTo(command.title)
            }
        }
    }

    @Nested
    inner class `문의 내역을 조회할 때` {

        @Test
        fun `DB로부터 조회하여 결과를 반환한다`() {
            // given
            val userId = user.id
            val title = "안녕하세요"
            val body = "잘부탁드립니다."

            inquiryRepository.save(
                InquiryQuestion(
                    user = user,
                    title = title,
                    body = body
                )
            )

            // when
            val actual = inquiryService.getInquiry(userId)

            // then
            SoftAssertions.assertSoftly { softly: SoftAssertions ->
                softly.assertThat(actual.inquiries[0].question.title).isEqualTo(title)
                softly.assertThat(actual.inquiries[0].question.body).isEqualTo(body)
                softly.assertThat(actual.inquiries[0].answer).isNull()
            }
        }

    }
}