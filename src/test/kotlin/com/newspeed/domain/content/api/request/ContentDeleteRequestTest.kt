package com.newspeed.domain.content.api.request

import com.newspeed.template.UnitTestTemplate
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import javax.validation.Validation
import javax.validation.Validator
import javax.validation.ValidatorFactory

@DisplayName("보관함 삭제 API request body 객체는 ")
class ContentDeleteRequestTest: UnitTestTemplate {

    companion object {
        private val factory: ValidatorFactory = Validation.buildDefaultValidatorFactory()
        private val validator: Validator = factory.validator

        @JvmStatic
        @AfterAll
        fun close() {
            factory.close()
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["", " "])
    fun `url을 입력하지 않으면 에러를 던진다`(
        url: String
    ) {
        // given
        val request = ContentDeleteRequest(
            url = url
        )

        // when
        val result = validator.validate(request)

        // then
        Assertions.assertThat(result.joinToString { it.messageTemplate }.contains("url"))
    }

    @Test
    fun `정상 입력값으로 요청하면 성공한다`() {
        // given
        val request = ContentDeleteRequest(
            url = "https://www.youtube.com"
        )

        // when
        val result = validator.validate(request)

        // then
        Assertions.assertThat(result).isEmpty()
    }
}