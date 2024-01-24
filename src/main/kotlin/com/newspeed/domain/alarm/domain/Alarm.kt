package com.newspeed.domain.alarm.domain

import com.newspeed.domain.alarm.application.command.AlarmUpdateCommand
import com.newspeed.domain.user.domain.User
import com.newspeed.global.model.BaseTimeEntity
import org.hibernate.annotations.Comment
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where
import java.sql.Time
import javax.persistence.*

@Entity
@Table(name = "alarm")
@SQLDelete(sql = "UPDATE newspeed.alarm SET newspeed.alarm.deleted_at = now() WHERE id = ?")
@Where(clause = "deleted_at is null")
class Alarm(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Comment("id")
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT), unique = true)
    @Comment("사용자 ID")
    var user: User,

    @Column(name = "start_time", nullable = false)
    @Comment("알람 시작 시간")
    var startTime: Time,

    @Column(name = "end_time", nullable = false)
    @Comment("알람 종료 시간")
    var endTime: Time
): BaseTimeEntity() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Alarm

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.toInt()
    }

    fun updateTime(
        that: AlarmUpdateCommand
    ) {
        this.startTime = that.startTime
        this.endTime = that.endTime
    }
}