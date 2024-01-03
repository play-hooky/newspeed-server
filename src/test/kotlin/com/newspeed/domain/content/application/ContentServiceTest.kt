package com.newspeed.domain.content.application

import com.newspeed.domain.content.dto.toRecommendQueryResponse
import com.newspeed.domain.content.repository.QueryHistoryRepository
import com.newspeed.domain.user.application.UserService
import com.newspeed.factory.auth.AuthFactory.Companion.createKakaoUser
import com.newspeed.factory.content.ContentFactory.Companion.createQueryHistories
import com.newspeed.factory.content.ContentFactory.Companion.createQueryHistoryResponse
import com.newspeed.factory.content.ContentFactory.Companion.createRecommendQueryDTOs
import com.newspeed.factory.content.DummyContentClient
import com.newspeed.global.exception.content.NotFoundQueryHistoryException
import com.newspeed.global.exception.content.UnavailableDeleteQueryHistoryException
import com.newspeed.global.exception.user.UserNotFoundException
import com.newspeed.template.UnitTestTemplate
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.mock
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDate

@DisplayName("content 서비스 계층에서 ")
class ContentServiceTest: UnitTestTemplate {

    private val dummyContentClient = DummyContentClient()
    private val contentSearchClients = ContentSearchClients.builder()
        .add(dummyContentClient)
        .build()

    private val userService = mock(UserService::class.java)
    private val queryHistoryRepository = mockk<QueryHistoryRepository>(relaxed = true)
    private val applicationEventPublisher = mock(ApplicationEventPublisher::class.java)
    private val contentService = ContentService(
        contentSearchClients = contentSearchClients,
        userService = userService,
        queryHistoryRepository = queryHistoryRepository,
        eventPublisher = applicationEventPublisher
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

            every { queryHistoryRepository.findByUser(user) } returns queryHistories

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

    @Nested
    inner class `추천 검색어를 조회할 때` {
        @Test
        fun `오늘 가장 많이 검색한 검색어를 조회한다`() {
            // given
            val date = LocalDate.now()
            val size = 5
            val recommendQueryDTOs = createRecommendQueryDTOs(size)
            val expected = recommendQueryDTOs.toRecommendQueryResponse()

            every { queryHistoryRepository.findDailyMaxQueryHistory(date, size) } returns recommendQueryDTOs

            // when
            val actual = contentService.recommendQuery(date, size)

            assertThat(expected).usingRecursiveComparison().isEqualTo(actual)
        }
    }

    @Nested
    inner class `검색 기록을 삭제할 때` {

        @Test
        fun `해당 사용자가 검색한 기록이면 삭제한다`() {
            // given
            val userId = 1L
            val user = createKakaoUser()
            val queryHistories = createQueryHistories(user)

            given(userService.getUser(userId))
                .willReturn(user)

            queryHistories.forEach{
                every { queryHistoryRepository.findByIdOrNull(it.id) } returns it
            }

            // when
            queryHistories.forEach{
                contentService.deleteQueryHistory(userId, it.id)
            }

            // then
            queryHistories.forEach{
                assertThat(it.deletedAt()).isNotNull()
            }
        }

        @Test
        fun `해당 사용자가 검색한 기록이 아니면 에러를 던진다`() {
            // given
            val userId = 1L
            val user = createKakaoUser()
            val wrongUser = createKakaoUser()
            wrongUser.id = Long.MIN_VALUE
            val queryHistories = createQueryHistories(wrongUser)

            given(userService.getUser(userId))
                .willReturn(user)

            queryHistories.forEach{
                every { queryHistoryRepository.findByIdOrNull(it.id) } returns it
            }

            // when & then
            queryHistories.forEach{
                assertThatThrownBy { contentService.deleteQueryHistory(userId, it.id) }
                    .isInstanceOf(UnavailableDeleteQueryHistoryException::class.java)
            }
        }

        @Test
        fun `사용자가 존재하지 않으면 에러를 던진다`() {
            // given
            val userId = Long.MIN_VALUE

            given(userService.getUser(userId))
                .willThrow(UserNotFoundException())

            // when & then
            assertThatThrownBy { contentService.deleteQueryHistory(userId, 1L) }
                .isInstanceOf(UserNotFoundException::class.java)
        }

        @Test
        fun `검색 결과가 존재하지 않으면 에러를 던진다`() {
            val userId = 1L
            val user = createKakaoUser()
            val queryHistoryId = 1L

            given(userService.getUser(userId))
                .willReturn(user)

            every { queryHistoryRepository.findByIdOrNull(queryHistoryId) } throws NotFoundQueryHistoryException()

            // when & then
            assertThatThrownBy { contentService.deleteQueryHistory(userId, queryHistoryId) }
                .isInstanceOf(NotFoundQueryHistoryException::class.java)
        }
    }
}