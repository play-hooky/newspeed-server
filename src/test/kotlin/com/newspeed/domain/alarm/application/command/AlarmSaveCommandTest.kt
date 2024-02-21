package com.newspeed.domain.alarm.application.command

import com.newspeed.domain.alarm.domain.Alarm
import com.newspeed.domain.alarm.repository.AlarmRepository
import com.newspeed.domain.user.domain.User
import com.newspeed.domain.user.repository.UserRepository
import com.newspeed.factory.auth.AuthFactory.Companion.createKakaoUser
import com.newspeed.template.IntegrationTestTemplate
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import java.sql.Time
import java.time.LocalTime
import javax.validation.Validator

@DisplayName("알람 등록 service command 객체는 ")
class AlarmSaveCommandTest: IntegrationTestTemplate {

    @Autowired
    private lateinit var validator: Validator

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var alarmRepository: AlarmRepository

    private lateinit var user: User

    private lateinit var alarm: Alarm

    @BeforeEach
    fun setup() {
        user = userRepository.save(
            createKakaoUser()
        )

        alarm = alarmRepository.save(
            Alarm(
                user = user,
                startTime = Time.valueOf(LocalTime.now()),
                endTime = Time.valueOf(LocalTime.now().plusHours(2)),
            )
        )
    }

    @ParameterizedTest
    @ValueSource(longs = [-1, 0])
    fun `유효하지 않은 userId를 입력하면 에러를 던진다`(
        userId: Long
    ) {
        // given
        val command = AlarmSaveCommand(
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
    fun `이미 등록된 알람이면 에러를 던진다`() {
        // given
        val command = AlarmSaveCommand(
            userId = alarm.user.id,
            startTime = Time.valueOf(LocalTime.now()),
            endTime = Time.valueOf(LocalTime.now()),
        )

        // when
        val result = validator.validate(command)

        // when
        Assertions.assertThat(result.joinToString { it.messageTemplate }).contains("해당 사용자는 이미 알람이 등록되어 있습니다.")
    }

    @Test
    fun `정상 입력값으로 요청하면 성공한다`() {
        // given
        val command = AlarmSaveCommand(
            userId = user.id,
            startTime = Time.valueOf(LocalTime.now()),
            endTime = Time.valueOf(LocalTime.now()),
        )
        val alarm = alarmRepository.findByUser(user)
        alarm?.delete()
        alarmRepository.save(alarm)

        // when
        val result = validator.validate(command)

        // then
        Assertions.assertThat(result).isEmpty()
    }
}