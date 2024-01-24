package com.newspeed.domain.alarm.application

import com.newspeed.domain.alarm.application.command.AlarmSaveCommand
import com.newspeed.domain.alarm.repository.AlarmRepository
import com.newspeed.domain.user.application.UserService
import com.newspeed.factory.alarm.AlarmFactory.Companion.createAlarm
import com.newspeed.factory.auth.AuthFactory.Companion.createKakaoUser
import com.newspeed.template.UnitTestTemplate
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
}