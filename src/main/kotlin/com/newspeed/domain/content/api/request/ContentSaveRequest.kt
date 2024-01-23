package com.newspeed.domain.content.api.request

import com.newspeed.domain.content.application.command.ContentSaveCommand
import javax.validation.constraints.NotBlank

data class ContentSaveRequest(
    @field:NotBlank(message = "contentIdInPlatform을 입력해주세요.")
    val contentIdInPlatform: String,
    @field:NotBlank(message = "url을 입력해주세요.")
    val url: String
) {
    fun toCommand(
        userId: Long
    ): ContentSaveCommand = ContentSaveCommand(
        userId = userId,
        contentIdInPlatform = contentIdInPlatform,
        url = url
    )
}
