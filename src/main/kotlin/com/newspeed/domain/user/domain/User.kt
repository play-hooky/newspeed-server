package com.newspeed.domain.user.domain

import com.newspeed.domain.auth.api.response.UserResponse
import com.newspeed.domain.auth.domain.AuthPayload
import com.newspeed.domain.auth.domain.enums.LoginPlatform
import com.newspeed.domain.auth.domain.enums.Role
import com.newspeed.global.model.BaseTimeEntity
import org.hibernate.annotations.Comment
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where
import javax.persistence.*

@Entity
@Table(name = "\"user\"")
@SQLDelete(sql = "UPDATE newspeed.user SET newspeed.user.deleted_at = now() WHERE id = ?")
@Where(clause = "deleted_at is null")
class User(
    @Id
    @Comment("사용자 ID")
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Comment("사용자 이메일")
    @Column(name = "email", length = 128, nullable = false)
    var email: String,

    @Comment("사용자 닉네임")
    @Column(name = "nickname", length = 128, nullable = false)
    var nickname: String,

    @Comment("SNS 플랫폼 KAKAO, APPLE")
    @Enumerated(EnumType.STRING)
    @Column(name = "platform", nullable = false, columnDefinition = "enum('KAKAO','APPLE','NEWSPEED')")
    var platform: LoginPlatform,

    @Comment("프로필 사진 url")
    @Column(name = "profile_image_url", length = 256)
    var profileImageUrl: String?,

    @Comment("사용자 권한 USER, ADMIN")
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, columnDefinition = "enum('ANONYMOUS','USER','ADMIN')")
    var role: Role,
): BaseTimeEntity() {
    fun toAuthPayload(
    ): AuthPayload = AuthPayload(
        userId = id,
        role = role,
        loginPlatform = platform,
        email = email
    )

    fun toResponse(): UserResponse = UserResponse(
        email = email,
        profileImgUrl = profileImageUrl,
        nickname = nickname
    )
}