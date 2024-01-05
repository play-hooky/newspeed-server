package com.newspeed.domain.content.application.command

import com.newspeed.domain.content.domain.Content
import com.newspeed.domain.content.domain.enums.QueryPlatform
import com.newspeed.domain.content.repository.ContentRepository
import com.newspeed.domain.user.repository.UserRepository
import com.newspeed.factory.auth.AuthFactory.Companion.createKakaoUser
import com.newspeed.template.IntegrationTestTemplate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import javax.validation.Validator

@DisplayName("컨텐츠 저장 service command 객체는 ")
class ContentSaveCommandTest: IntegrationTestTemplate {

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
        val command = ContentSaveCommand(
            userId = userId,
            url = VALID_URL
        )

        // when
        val result = validator.validate(command)

        // then
        assertThat(result.joinToString { it.messageTemplate }).contains("사용자")
    }

    @ParameterizedTest
    @ValueSource(strings = ["", " "])
    fun `url을 입력하지 않으면 에러를 던진다`(
        url: String
    ) {
        // given
        val command = ContentSaveCommand(
            userId = 1L,
            url = url
        )

        // when
        val result = validator.validate(command)

        // when
        assertThat(result.joinToString { it.messageTemplate }).contains("url")
    }

    @Test
    fun `이미 등록된 컨텐츠면 에러를 던진다`() {
        // given
        val content = saveContent()

        val command = ContentSaveCommand(
            userId = content.user.id,
            url = VALID_URL
        )

        // when
        val result = validator.validate(command)

        // when
        assertThat(result.joinToString { it.messageTemplate }).contains("이미 등록된 컨텐츠입니다")
    }

    @Test
    fun `정상 입력값으로 요청하면 성공한다`() {
        // given
        val command = ContentSaveCommand(
            userId = 1L,
            url = VALID_URL
        )

        // when
        val result = validator.validate(command)

        // then
        assertThat(result).isEmpty()
    }

    private fun saveContent(): Content {
        val user = createKakaoUser()
        userRepository.save(user)

        return contentRepository.save(
            Content(
                user = user,
                url = VALID_URL,
                platform = QueryPlatform.YOUTUBE
            )
        )
    }
}