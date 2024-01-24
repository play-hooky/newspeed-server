package com.newspeed.domain.alarm.application

import com.newspeed.domain.alarm.application.command.AlarmSaveCommand
import com.newspeed.domain.alarm.application.command.AlarmUpdateCommand
import com.newspeed.domain.alarm.repository.AlarmRepository
import com.newspeed.domain.user.application.UserService
import com.newspeed.factory.alarm.AlarmFactory.Companion.createAlarm
import com.newspeed.factory.auth.AuthFactory.Companion.createKakaoUser
import com.newspeed.global.exception.alarm.NotFoundAlarmException
import com.newspeed.template.UnitTestTemplate
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.times
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import java.sql.Time
import java.time.LocalTime

@DisplayName("alarm 서비스 계층에서 ")
class AlarmServiceTest: UnitTestTemplate {

    @Mock
    private lateinit var alarmRepository: AlarmRepository

    @Mock
    private lateinit var userService: UserService

    @InjectMocks
    private lateinit var alarmService: AlarmService

    @Nested
    inner class `알람을 저장할 때` {

        @Test
        fun `사용자를 조회하여 알람을 저장한다`() {
            // given
            val user = createKakaoUser()
            val command = AlarmSaveCommand(
                userId = user.id,
                startTime = Time.valueOf(LocalTime.now()),
                endTime = Time.valueOf(LocalTime.now().plusHours(2)),
            )
            val alarm = createAlarm(user)

            given(userService.getUser(command.userId))
                .willReturn(user)

            // when
            alarmService.saveAlarm(command)

            // then
            Mockito.verify(alarmRepository, times(1)).save(alarm)

        }
    }

    @Nested
    inner class `알림을 수정할 때` {

        @Test
        fun `알람이 존재하지 않으면 에러를 던진다`() {
            // given
            val user = createKakaoUser()
            val command = AlarmUpdateCommand(
                userId = user.id,
                startTime = Time.valueOf(LocalTime.now()),
                endTime = Time.valueOf(LocalTime.now().plusHours(2)),
            )

            given(userService.getUser(command.userId))
                .willReturn(user)

            given(alarmRepository.findByUser(user))
                .willReturn(null)

            // when & then
            Assertions.assertThatThrownBy { alarmService.updateAlarm(command) }
                .isInstanceOf(NotFoundAlarmException::class.java)
        }

        @Test
        fun `알람이 존재하면 수정에 성공한다`() {
            // given
            val user = createKakaoUser()
            val command = AlarmUpdateCommand(
                userId = user.id,
                startTime = Time.valueOf(LocalTime.now()),
                endTime = Time.valueOf(LocalTime.now().plusHours(2)),
            )
            val alarm = createAlarm(user)

            given(userService.getUser(command.userId))
                .willReturn(user)

            given(alarmRepository.findByUser(user))
                .willReturn(alarm)

            // when
            alarmService.updateAlarm(command)

            Assertions.assertThat(alarm.startTime).isEqualTo(command.startTime)
            Assertions.assertThat(alarm.endTime).isEqualTo(command.endTime)
        }
    }

    @Nested
    inner class `알림을 삭제할 때` {

        @Test
        fun `알림이 존재하지 않으면 삭제에 실패한다`() {
            // given
            val user = createKakaoUser()

            given(userService.getUser(user.id))
                .willReturn(user)

            given(alarmRepository.findByUser(user))
                .willReturn(null)

            // when & then
            Assertions.assertThatThrownBy { alarmService.deleteAlarm(user.id) }
                .isInstanceOf(NotFoundAlarmException::class.java)
        }

        @Test
        fun `알림이 존재하면 삭제에 성공한다`() {
            // given
            val user = createKakaoUser()
            val alarm = createAlarm(user)

            given(userService.getUser(user.id))
                .willReturn(user)

            given(alarmRepository.findByUser(user))
                .willReturn(alarm)

            // when
            alarmService.deleteAlarm(user.id)

            // then
            Assertions.assertThat(alarm.deletedAt()).isNotNull()
        }
    }
}