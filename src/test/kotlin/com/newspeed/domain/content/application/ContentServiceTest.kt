package com.newspeed.domain.content.application

import com.newspeed.domain.content.repository.QueryHistoryRepository
import com.newspeed.domain.user.application.UserService
import com.newspeed.factory.auth.AuthFactory.Companion.createKakaoUser
import com.newspeed.factory.content.ContentFactory.Companion.createQueryHistories
import com.newspeed.factory.content.ContentFactory.Companion.createQueryHistoryResponse
import com.newspeed.factory.content.DummyContentClient
import com.newspeed.global.exception.user.UserNotFoundException
import com.newspeed.template.UnitTestTemplate
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.mock

@DisplayName("content 서비스 계층에서 ")
class ContentServiceTest: UnitTestTemplate {

    private val dummyContentClient = DummyContentClient()
    private val contentSearchClients = ContentSearchClients.builder()
        .add(dummyContentClient)
        .build()

    private val userService = mock(UserService::class.java)
    private val queryHistoryRepository = mock(QueryHistoryRepository::class.java)
    private val contentService = ContentService(
        contentSearchClients = contentSearchClients,
        userService = userService,
        queryHistoryRepository = queryHistoryRepository
    )

    @Nested
    inner class `검색 기록을 조회할 때` {

        @Test
        fun `사용자가 존재하면 검색 기록을 반환한다`() {
            // given
            val userId = 1L
            val user = createKakaoUser()
            val queryHistories = createQueryHistories(user)
            val expected = createQueryHistoryResponse()

            given(userService.getUser(userId))
                .willReturn(user)

            given(queryHistoryRepository.findByUser(user))
                .willReturn(queryHistories)

            // when
            val actual = contentService.getQueryHistory(userId)

            // then
            assertThat(expected).usingRecursiveComparison().isEqualTo(actual)
        }

        @Test
        fun `사용자가 존재하지 않으면 에러를 던진다`() {
            // given
            val userId = Long.MIN_VALUE

            given(userService.getUser(userId))
                .willThrow(UserNotFoundException())

            // when & then
            assertThatThrownBy { contentService.getQueryHistory(userId) }
                .isInstanceOf(UserNotFoundException::class.java)
        }
    }
}