package com.newspeed.domain.alarm.api

import com.newspeed.domain.alarm.api.request.AlarmSaveRequest
import com.newspeed.domain.alarm.application.AlarmService
import com.newspeed.domain.jwt.annotation.User
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/user/alarm")
class AlarmController(
    private val alarmService: AlarmService
) {

    @PostMapping
    fun saveAlarm(
        @User userId: Long,
        @Valid @RequestBody request: AlarmSaveRequest
    ): ResponseEntity<Unit> {
        alarmService.saveAlarm(
            request.toCommand(userId)
        )

        return ResponseEntity(HttpStatus.CREATED)
    }
}