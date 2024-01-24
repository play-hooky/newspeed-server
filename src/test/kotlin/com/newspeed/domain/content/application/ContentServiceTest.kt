package com.newspeed.domain.content.application

import com.newspeed.domain.content.application.command.ContentDeleteCommand
import com.newspeed.domain.content.domain.enums.QueryPlatform
import com.newspeed.domain.content.repository.ContentRepository
import com.newspeed.domain.search.application.SearchService
import com.newspeed.domain.user.application.UserService
import com.newspeed.factory.auth.AuthFactory.Companion.createKakaoUser
import com.newspeed.factory.content.ContentFactory.Companion.createContentSaveCommand
import com.newspeed.factory.content.ContentFactory.Companion.createContents
import com.newspeed.factory.content.ContentFactory.Companion.createContentsResponse
import com.newspeed.global.exception.content.NotFoundContentException
import com.newspeed.global.exception.model.ExceptionType
import com.newspeed.template.UnitTestTemplate
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.*
import org.mockito.Mockito

@DisplayName("content 서비스 계층에서 ")
class ContentServiceTest: UnitTestTemplate {
    private val userService = mock(UserService::class.java)
    private val searchService = mock(SearchService::class.java)
    private val contentRepository = mock(ContentRepository::class.java)
    private val contentService = ContentService(
        userService = userService,
        searchService = searchService,
        contentRepository = contentRepository
    )

    @Nested
    inner class `컨텐츠를 저장할 때` {

        @Test
        fun `url이 존재하면 저장에 성공한다`() {
            // given
            val userId = 1L
            val user = createKakaoUser()
            val command = createContentSaveCommand(userId)

            given(userService.getUser(userId))
                .willReturn(user)

            // when
            contentService.saveContent(command)

            // then
            Mockito.verify(contentRepository, times(1)).save(any())
        }
    }

    @Nested
    inner class `보관함을 조회할 때` {

        @Test
        fun `DB로부터 조회 후 필요한 정보를 유튜브에 요청하여 조합한다`() {
            val userId = 1L
            val user = createKakaoUser()
            val contents = createContents(user)
            val expected = createContentsResponse()

            given(contentRepository.findByUserId(userId))
                .willReturn(contents)

            given(searchService.search(QueryPlatform.NEWSPEED, contents.map { it.contentIdInPlatform }))
                .willReturn(expected.contentResponses)

            // when
            val actual = contentService.getContents(userId)

            // then
            assertThat(actual).usingRecursiveComparison().isEqualTo(expected)
        }
    }

    @Nested
    inner class `보관함을 삭제할 때` {
        val url = "https://www.youtube.com"

        @Test
        fun `해당 사용자의 컨텐츠가 존재하지 않으면 삭제에 실패한다`() {
            // given
            val user = createKakaoUser()
            val command = ContentDeleteCommand(
                userId = user.id,
                url = url
            )

            given(userService.getUser(user.id))
                .willReturn(user)

            given(contentRepository.findByUserAndUrl(user, url))
                .willReturn(emptyList())

            // when
            assertThatThrownBy { contentService.deleteContent(command) }
                .isInstanceOf(NotFoundContentException::class.java)
                .hasMessage(ExceptionType.NOT_FOUND_CONTENT_EXCEPTION.message)
        }

        @Test
        fun `해당 사용자의 컨텐츠가 존재하면 삭제에 성공한다`() {
            // given
            val user = createKakaoUser()
            val contents = createContents(user)
            val command = ContentDeleteCommand(
                userId = user.id,
                url = url
            )

            given(userService.getUser(user.id))
                .willReturn(user)

            given(contentRepository.findByUserAndUrl(user, url))
                .willReturn(contents)

            // when
            contentService.deleteContent(command)

            // then
            verify(contentRepository, times(1)).deleteByIds(contents.map { it.id })
        }
    }
}