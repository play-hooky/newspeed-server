package com.newspeed.domain.alarm.api.request

import com.newspeed.template.UnitTestTemplate
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.time.LocalDateTime
import javax.validation.Validation
import javax.validation.Validator
import javax.validation.ValidatorFactory

@DisplayName("알람 수정 API request body 객체는 ")
class AlarmUpdateRequestTest: UnitTestTemplate {

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
    @ValueSource(strings = ["09:61", "19:68", "", " ", ":", "123:123"])
    fun `유효하지 않은 알람 시작 시간을 입력하면 에러를 던진다`(
        startTime: String
    ) {
        // given
        val request = AlarmUpdateRequest(
            startTime = startTime,
            endTime = "${LocalDateTime.now().hour}:${LocalDateTime.now().minute}"
        )

        // when
        val result = validator.validate(request)

        // then
        Assertions.assertThat(result.joinToString { it.messageTemplate }.contains("startTime")).isTrue()
    }

    @ParameterizedTest
    @ValueSource(strings = ["09:61", "19:68", "", " ", ":", "123:123"])
    fun `유효하지 않은 알람 종료 시간을 입력하면 에러를 던진다`(
        endTime: String
    ) {
        // given
        val request = AlarmUpdateRequest(
            startTime = "${LocalDateTime.now().hour}:${LocalDateTime.now().minute}",
            endTime = endTime
        )

        // when
        val result = validator.validate(request)

        // then
        Assertions.assertThat(result.joinToString { it.messageTemplate }.contains("endTime")).isTrue()
    }

    @Test
    fun `정상 입력값으로 요청하면 성공한다`() {
        // given
        val request = AlarmUpdateRequest(
            startTime = "${LocalDateTime.now().hour}:${LocalDateTime.now().minute}",
            endTime = "${LocalDateTime.now().hour}:${LocalDateTime.now().minute}",
        )

        // when
        val result = validator.validate(request)

        // then
        Assertions.assertThat(result).isEmpty()
    }
}