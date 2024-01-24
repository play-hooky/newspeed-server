package com.newspeed.domain.alarm.api

import com.newspeed.domain.alarm.api.request.AlarmSaveRequest
import com.newspeed.domain.alarm.api.request.AlarmUpdateRequest
import com.newspeed.domain.alarm.api.response.AlarmResponse
import com.newspeed.domain.alarm.application.AlarmService
import com.newspeed.domain.jwt.annotation.User
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
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

    @GetMapping
    fun getAlarm(
        @User userId: Long
    ): ResponseEntity<AlarmResponse> = ResponseEntity.ok(
        alarmService.getAlarm(userId)
    )

    @PutMapping
    fun updateAlarm(
        @User userId: Long,
        @Valid @RequestBody request: AlarmUpdateRequest
    ): ResponseEntity<Unit> {
        alarmService.updateAlarm(
            request.toCommand(userId)
        )

        return ResponseEntity(HttpStatus.CREATED)
    }

    @DeleteMapping
    fun deleteAlarm(
        @User userId: Long
    ): ResponseEntity<Unit> {
        alarmService.deleteAlarm(userId)

        return ResponseEntity(HttpStatus.OK)
    }
}