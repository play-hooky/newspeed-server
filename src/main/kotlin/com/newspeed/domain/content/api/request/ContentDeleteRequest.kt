package com.newspeed.domain.content.api.request

import com.newspeed.domain.content.application.command.ContentDeleteCommand
import javax.validation.constraints.NotBlank

data class ContentDeleteRequest(
    @field:NotBlank(message = "url을 입력해주세요.")
    val url: String
) {
    fun toCommand(
        userId: Long
    ): ContentDeleteCommand = ContentDeleteCommand(
        url = url,
        userId = userId
    )
}
