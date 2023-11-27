package com.newspeed.global.model

import org.hibernate.annotations.Comment
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseTimeEntity(
    @CreatedDate
    @Comment("생성한 시간")
    @Column(name = "created_at", nullable = false)
    protected var createdAt: LocalDateTime = LocalDateTime.now(),

    @LastModifiedDate
    @Comment("수정한 시간")
    @Column(name = "updated_at", nullable = false)
    protected var updatedAt: LocalDateTime = LocalDateTime.now(),

    @Comment("삭제한 시간")
    @Column(name = "deleted_at")
    protected var deletedAt: LocalDateTime? = null
)