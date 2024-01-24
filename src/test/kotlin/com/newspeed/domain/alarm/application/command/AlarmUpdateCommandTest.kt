package com.newspeed.domain.alarm.application.command

import com.newspeed.domain.alarm.domain.Alarm
import com.newspeed.domain.alarm.repository.AlarmRepository
import com.newspeed.domain.user.repository.UserRepository
import com.newspeed.factory.auth.AuthFactory
import com.newspeed.factory.auth.AuthFactory.Companion.createKakaoUser
import com.newspeed.template.IntegrationTestTemplate
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import java.sql.Time
import java.time.LocalTime
import javax.validation.Validator

@DisplayName("알람 수정 service command 객체는 ")
@Transactional
class AlarmUpdateCommandTest: IntegrationTestTemplate {

    @Autowired
    private lateinit var validator: Validator

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var alarmRepository: AlarmRepository

    @ParameterizedTest
    @ValueSource(longs = [-1, 0])
    fun `유효하지 않은 userId를 입력하면 에러를 던진다`(
        userId: Long
    ) {
        // given
        val command = AlarmUpdateCommand(
            userId = userId,
            startTime = Time.valueOf(LocalTime.now()),
            endTime = Time.valueOf(LocalTime.now()),
        )

        // when
        val result = validator.validate(command)

        // then
        Assertions.assertThat(result.joinToString { it.messageTemplate }).contains("사용자")
    }

    @Test
    fun `등록된 알람이 존재하지 않으면 에러를 던진다`() {
        // given
        val user = createKakaoUser()
        val command = AlarmUpdateCommand(
            userId = user.id,
            startTime = Time.valueOf(LocalTime.now()),
            endTime = Time.valueOf(LocalTime.now()),
        )

        // when
        val result = validator.validate(command)

        // when
        Assertions.assertThat(result.joinToString { it.messageTemplate }).contains("해당 사용자는 등록된 알람이 존재하지 않습니다.")
    }

    @Test
    fun `정상 입력값으로 요청하면 성공한다`() {
        // given
        val alarm = saveAlarm()
        val command = AlarmUpdateCommand(
            userId = alarm.user.id,
            startTime = Time.valueOf(LocalTime.now().minusHours(1)),
            endTime = Time.valueOf(LocalTime.now().plusHours(3)),
        )

        // when
        val result = validator.validate(command)

        // then
        Assertions.assertThat(result).isEmpty()
    }

    private fun saveAlarm(): Alarm {
        val user = AuthFactory.createKakaoUser()
        userRepository.save(user)

        return alarmRepository.save(
            Alarm(
                user = user,
                startTime = Time.valueOf(LocalTime.now()),
                endTime = Time.valueOf(LocalTime.now().plusHours(2)),
            )
        )
    }
}