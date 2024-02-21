package com.newspeed.domain.content.application

import com.newspeed.domain.content.application.command.ContentDeleteCommand
import com.newspeed.domain.content.application.command.ContentSaveCommand
import com.newspeed.domain.content.domain.Content
import com.newspeed.domain.content.domain.enums.QueryPlatform
import com.newspeed.domain.content.repository.ContentRepository
import com.newspeed.domain.user.domain.User
import com.newspeed.domain.user.repository.UserRepository
import com.newspeed.factory.auth.AuthFactory.Companion.createKakaoUser
import com.newspeed.factory.content.ContentFactory
import com.newspeed.global.exception.content.NotFoundContentException
import com.newspeed.template.IntegrationTestTemplate
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDateTime

@DisplayName("Content 서비스 계층 내")
class ContentServiceIntegrationTest: IntegrationTestTemplate {

    @Autowired
    private lateinit var contentService: ContentService

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var contentRepository: ContentRepository

    private lateinit var user: User

    @BeforeEach
    fun setup() {
        user = userRepository.save(createKakaoUser())
    }

    @Nested
    inner class `Content를 저장할 때` {

        @Test
        fun `사용자를 조회하여 DB에 저장한다`() {
            // given
            val contentId = "1"
            val url = "https://www.newspeed.store/hooky"
            val command = ContentSaveCommand(
                userId = user.id,
                contentIdInPlatform = contentId,
                url = url
            )
            val expected = Content(
                user = user,
                contentIdInPlatform = contentId,
                url = url,
                platform = QueryPlatform.NEWSPEED
            )

            // when
            contentService.saveContent(command)

            // then
            val contents = contentRepository.findByUserId(user.id)

            assertThat(contents.find { it.user == user })
                .usingRecursiveComparison()
                .ignoringFields("id")
                .ignoringFieldsOfTypes(LocalDateTime::class.java)
                .isEqualTo(expected)
        }

        @Test
        fun `저장하려는 사용자의 ID가 존재하지 않으면 에러를 던진다`() {
            // given
            val command = ContentSaveCommand(
                userId = -1L,
                contentIdInPlatform = "1",
                url = "https://www.newspeed.store/hooky"
            )

            // when & then
            assertThatThrownBy { contentService.saveContent(command) }
                .hasMessageContaining("userId")
        }

        @Test
        fun `저장하려는 ContentID가 존재하지 않으면 에러를 던진다`() {
            // given
            val command = ContentSaveCommand(
                userId = user.id,
                contentIdInPlatform = "",
                url = "https://www.newspeed.store/hooky"
            )

            // when & then
            assertThatThrownBy { contentService.saveContent(command) }
                .hasMessageContaining("contentIdInPlatform")
        }

        @Test
        fun `저장하려는 Content의 url이 존재하지 않으면 에러를 던진다`() {
            // given
            val command = ContentSaveCommand(
                userId = user.id,
                contentIdInPlatform = "1",
                url = ""
            )

            // when & then
            assertThatThrownBy { contentService.saveContent(command) }
                .hasMessageContaining("url")
        }

        @Test
        fun `이미 저장되어 있는 Content면 에러를 던진다`() {
            // given
            val command = ContentSaveCommand(
                userId = user.id,
                contentIdInPlatform = "1",
                url = "https://www.newspeed.store/hooky"
            )

            contentService.saveContent(command)

            // when & then
            assertThatThrownBy { contentService.saveContent(command) }
                .hasMessageContaining("이미 등록된 컨텐츠입니다")
        }
    }

    @Nested
    inner class `Content를 조회할 때` {

        @Test
        fun `사용자 ID를 바탕으로 DB에서 조회하여 결과를 반환한다`() {
            // given
            val userId = user.id
            val contents = ContentFactory.createContents(user)
            contentRepository.saveAll(contents)

            // when
            val actual = contentService.getContents(userId)

            // then
            SoftAssertions.assertSoftly { softly: SoftAssertions ->
                softly.assertThat(actual.contentResponses.get(0).platform).isEqualTo(contents.get(0).platform)
                softly.assertThat(actual.contentResponses.get(0).youtube?.id).isEqualTo(contents.get(0).contentIdInPlatform)
                softly.assertThat(actual.contentResponses.get(0).youtube?.url).isEqualTo(contents.get(0).url)
            }
        }
    }

    @Nested
    inner class `Content를 삭제할 때` {

        @Test
        fun `해당 사용자가 저장한 Content가 아니면 에러를 던진다`() {
            // given
            val command = ContentDeleteCommand(
                userId = user.id,
                url = "https://www.newspeed.store/hooky"
            )

            // when & then
            assertThatThrownBy { contentService.deleteContent(command) }
                .isInstanceOf(NotFoundContentException::class.java)
        }

        @Test
        fun `삭제하려는 Content url이 존재하지 않으면 에러를 던진다`() {
            // given
            val command = ContentDeleteCommand(
                userId = user.id,
                url = ""
            )

            // when & then
            assertThatThrownBy { contentService.deleteContent(command) }
                .hasMessageContaining("url")
        }

        @Test
        fun `삭제하려는 사용자 ID가 존재하지 않으면 에러를 던진다`() {
            // given
            val command = ContentDeleteCommand(
                userId = -1L,
                url = ""
            )

            // when & then
            assertThatThrownBy { contentService.deleteContent(command) }
                .hasMessageContaining("userId")
        }

        @Test
        fun `DB에서 조회하여 Content를 삭제한다`() {
            // given
            val url = "https://www.newspeed.store/hooky"
            val command = ContentDeleteCommand(
                userId = user.id,
                url = url
            )

            contentRepository.save(Content(
                user = user,
                contentIdInPlatform = "1",
                url = url,
                platform = QueryPlatform.NEWSPEED
            ))

            // when
            contentService.deleteContent(command)

            // then
            val contents = contentRepository.findByUserId(command.userId)
            assertThat(contents.find { it.url == command.url }).isNull()
        }
    }
}