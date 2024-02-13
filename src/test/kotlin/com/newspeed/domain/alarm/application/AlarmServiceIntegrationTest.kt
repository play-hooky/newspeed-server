package com.newspeed.domain.alarm.application

import com.newspeed.domain.alarm.application.command.AlarmSaveCommand
import com.newspeed.domain.alarm.application.command.AlarmUpdateCommand
import com.newspeed.domain.alarm.domain.Alarm
import com.newspeed.domain.alarm.repository.AlarmRepository
import com.newspeed.domain.user.domain.User
import com.newspeed.domain.user.repository.UserRepository
import com.newspeed.factory.auth.AuthFactory.Companion.createKakaoUser
import com.newspeed.global.enums.toStringWithoutSeconds
import com.newspeed.global.exception.alarm.NotFoundAlarmException
import com.newspeed.template.IntegrationTestTemplate
import org.assertj.core.api.Assertions
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import java.sql.Time
import java.time.LocalDateTime

@DisplayName("Alarm Service 계층 내")
class AlarmServiceIntegrationTest: IntegrationTestTemplate {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var alarmService: AlarmService

    @Autowired
    private lateinit var alarmRepository: AlarmRepository

    private lateinit var user: User

    private lateinit var alarm: Alarm

    @BeforeEach
    fun setup(test: TestInfo) {
        user = userRepository.save(
            createKakaoUser()
        )

        if (listOf(
                "사용자 입력을 entity로 변환하여 DB에 저장한다",
                "알람이 존재하지 않으면 에러를 던진다"
        ).contains(test.testMethod.get().name)) {
            return;
        }

        alarm = alarmRepository.save(
            Alarm(
               user = user,
                startTime = Time.valueOf(LocalDateTime.now().plusHours(1).toLocalTime()),
                endTime = Time.valueOf(LocalDateTime.now().plusHours(6).toLocalTime())
            )
        )
    }

    @Nested
    inner class `알람을 저장할 때` {

        @Test
        fun `사용자 입력을 entity로 변환하여 DB에 저장한다`() {
            // given
            val command = AlarmSaveCommand(
                userId = user.id,
                startTime = Time.valueOf(LocalDateTime.now().plusHours(1).toLocalTime()),
                endTime = Time.valueOf(LocalDateTime.now().plusHours(6).toLocalTime())
            )

            // when
            alarmService.saveAlarm(command)

            // then
            val alarm = alarmRepository.findByUser(user)
            Assertions.assertThat(alarm).isNotNull
            SoftAssertions.assertSoftly { softly: SoftAssertions ->
                softly.assertThat(alarm?.user).isEqualTo(user)
                softly.assertThat(alarm?.startTime).isEqualTo(command.startTime)
                softly.assertThat(alarm?.endTime).isEqualTo(command.endTime)
            }
        }

        @Test
        fun `이미 등록된 알림이 있다면 에러를 던진다`() {
            // given
            val command = AlarmSaveCommand(
                userId = user.id,
                startTime = Time.valueOf(LocalDateTime.now().plusHours(1).toLocalTime()),
                endTime = Time.valueOf(LocalDateTime.now().plusHours(6).toLocalTime())
            )

            // when & then
            Assertions.assertThatThrownBy { alarmService.saveAlarm(command) }
                .hasMessageContaining("해당 사용자는 이미 알람이 등록되어 있습니다.")
        }
    }

    @Nested
    inner class `알림을 조회할 때` {

        @Test
        fun `DB에서 조회하여 결과를 반환한다`() {
            // given
            val userId = user.id

            // when
            val actual = alarmService.getAlarm(userId)

            // then
            SoftAssertions.assertSoftly { softly: SoftAssertions ->
                softly.assertThat(actual.startTime).isEqualTo(alarm.startTime.toStringWithoutSeconds())
                softly.assertThat(actual.endTime).isEqualTo(alarm.endTime.toStringWithoutSeconds())
            }
        }

        @Test
        fun `알람이 존재하지 않으면 에러를 던진다`() {
            // given
            val userId = user.id

             // when & then
            Assertions.assertThatThrownBy { alarmService.getAlarm(userId) }
                .isInstanceOf(NotFoundAlarmException::class.java)
        }
    }

    @Nested
    inner class `알림을 수정할 때` {

        @Test
        fun `조회한 기존 알림을 수정하여 DB에 반영한다`() {
            // given
            val command = AlarmUpdateCommand(
                userId = user.id,
                startTime = Time.valueOf(LocalDateTime.now().plusHours(3).toLocalTime()),
                endTime = Time.valueOf(LocalDateTime.now().plusHours(12).toLocalTime()),
            )

            // when
            alarmService.updateAlarm(command)

            // then
            val actual = alarmRepository.findByUser(user)

            Assertions.assertThat(actual).isNotNull
            SoftAssertions.assertSoftly { softly: SoftAssertions ->
                softly.assertThat(actual?.startTime).isEqualTo(command.startTime)
                softly.assertThat(actual?.endTime).isEqualTo(command.endTime)
            }
        }

        @Test
        fun `알람이 존재하지 않으면 에러를 던진다`() {
            // given
            val command = AlarmUpdateCommand(
                userId = user.id,
                startTime = Time.valueOf(LocalDateTime.now().plusHours(3).toLocalTime()),
                endTime = Time.valueOf(LocalDateTime.now().plusHours(12).toLocalTime()),
            )

            // when & then
            Assertions.assertThatThrownBy { alarmService.updateAlarm(command) }
                .isInstanceOf(NotFoundAlarmException::class.java)
        }
    }

    @Nested
    inner class `알림을 삭제할 때` {

        @Test
        fun `알람이 존재하지 않으면 에러를 던진다`() {
            // given
            val userId = user.id

            // when & then
            Assertions.assertThatThrownBy { alarmService.deleteAlarm(userId) }
                .isInstanceOf(NotFoundAlarmException::class.java)
        }

        @Test
        fun `알림을 삭제하여 DB에 반영한다` () {
            // given
            val userId = user.id

            // when
            alarmService.deleteAlarm(userId)

            // then
            Assertions.assertThat(alarmRepository.findByUser(user)).isNull()
        }
    }
}