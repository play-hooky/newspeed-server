package com.newspeed.domain.content.application.command

import com.newspeed.domain.content.application.command.validation.ContentSaveCommandConstraint
import com.newspeed.domain.content.domain.Content
import com.newspeed.domain.content.domain.enums.QueryPlatform
import com.newspeed.domain.user.domain.User
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive

@ContentSaveCommandConstraint
data class ContentSaveCommand(
    @field:Positive(message = "사용자를 식별할 수 없습니다.")
    val userId: Long,
    @field:NotBlank(message = "url을 입력해주세요.")
    val url: String
) {
    fun toContentEntity(
        user: User
    ): Content = Content(
        user = user,
        url = url,
        platform = QueryPlatform.findByUrl(url)
    )
}
