package com.newspeed.domain.content.application.command

import com.newspeed.domain.content.domain.Content
import com.newspeed.domain.content.domain.enums.QueryPlatform
import com.newspeed.domain.content.repository.ContentRepository
import com.newspeed.domain.user.repository.UserRepository
import com.newspeed.factory.auth.AuthFactory
import com.newspeed.template.IntegrationTestTemplate
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import javax.validation.Validator

@DisplayName("컨텐츠 삭제 service command 객체는 ")
class ContentDeleteCommandTest: IntegrationTestTemplate {

    @Autowired
    private lateinit var validator: Validator

    @Autowired
    private lateinit var contentRepository: ContentRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    companion object {
        private const val VALID_URL = "https://www.youtube.com"
    }

    @ParameterizedTest
    @ValueSource(longs = [-1, 0])
    fun `유효하지 않은 userId를 입력하면 에러를 던진다`(
        userId: Long
    ) {
        // given
        val command = ContentDeleteCommand(
            userId = userId,
            url = VALID_URL
        )

        // when
        val result = validator.validate(command)

        // then
        Assertions.assertThat(result.joinToString { it.messageTemplate }).contains("사용자")
    }

    @ParameterizedTest
    @ValueSource(strings = ["", " "])
    fun `url을 입력하지 않으면 에러를 던진다`(
        url: String
    ) {
        // given
        val command = ContentDeleteCommand(
            userId = 1L,
            url = url
        )

        // when
        val result = validator.validate(command)

        // when
        Assertions.assertThat(result.joinToString { it.messageTemplate }).contains("url")
    }

    @Test
    fun `정상 입력값으로 요청하면 성공한다`() {
        // given
        val content = saveContent()

        val command = ContentDeleteCommand(
            userId = content.user.id,
            url = content.url
        )

        // when
        val result = validator.validate(command)

        // then
        Assertions.assertThat(result).isEmpty()
    }

    private fun saveContent(): Content {
        val user = AuthFactory.createKakaoUser()
        userRepository.save(user)

        return contentRepository.save(
            Content(
                user = user,
                contentIdInPlatform = "hooky",
                url = VALID_URL,
                platform = QueryPlatform.YOUTUBE
            )
        )
    }
}