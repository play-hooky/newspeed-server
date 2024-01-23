package com.newspeed.domain.queryhistory.application

import com.newspeed.domain.content.dto.toRecommendQueryResponse
import com.newspeed.domain.content.repository.QueryHistoryRepository
import com.newspeed.domain.user.application.UserService
import com.newspeed.factory.auth.AuthFactory
import com.newspeed.factory.content.ContentFactory
import com.newspeed.global.exception.content.NotFoundQueryHistoryException
import com.newspeed.global.exception.content.UnavailableDeleteQueryHistoryException
import com.newspeed.global.exception.user.UserNotFoundException
import com.newspeed.template.UnitTestTemplate
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDate

@DisplayName("QueryHistory service 계층에서 ")
class QueryHistoryServiceTest: UnitTestTemplate {
    private val userService = BDDMockito.mock(UserService::class.java)
    private val queryHistoryRepository = mockk<QueryHistoryRepository>(relaxed = true)

    private val queryHistoryService = QueryHistoryService(
        userService = userService,
        queryHistoryRepository = queryHistoryRepository
    )

    @Nested
    inner class `검색 기록을 조회할 때` {

        @Test
        fun `사용자가 존재하면 검색 기록을 반환한다`() {
            // given
            val userId = 1L
            val user = AuthFactory.createKakaoUser()
            val queryHistories = ContentFactory.createQueryHistories(user)
            val expected = ContentFactory.createQueryHistoryResponse()

            BDDMockito.given(userService.getUser(userId))
                .willReturn(user)

            every { queryHistoryRepository.findByUser(user) } returns queryHistories

            // when
            val actual = queryHistoryService.getQueryHistory(userId)

            // then
            Assertions.assertThat(expected).usingRecursiveComparison().isEqualTo(actual)
        }

        @Test
        fun `사용자가 존재하지 않으면 에러를 던진다`() {
            // given
            val userId = Long.MIN_VALUE

            BDDMockito.given(userService.getUser(userId))
                .willThrow(UserNotFoundException())

            // when & then
            Assertions.assertThatThrownBy { queryHistoryService.getQueryHistory(userId) }
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
            val recommendQueryDTOs = ContentFactory.createRecommendQueryDTOs(size)
            val expected = recommendQueryDTOs.toRecommendQueryResponse()

            every { queryHistoryRepository.findDailyMaxQueryHistory(date, size) } returns recommendQueryDTOs

            // when
            val actual = queryHistoryService.recommendQuery(date, size)

            Assertions.assertThat(expected).usingRecursiveComparison().isEqualTo(actual)
        }
    }

    @Nested
    inner class `검색 기록을 삭제할 때` {

        @Test
        fun `해당 사용자가 검색한 기록이면 삭제한다`() {
            // given
            val userId = 1L
            val user = AuthFactory.createKakaoUser()
            val queryHistories = ContentFactory.createQueryHistories(user)

            BDDMockito.given(userService.getUser(userId))
                .willReturn(user)

            queryHistories.forEach{
                every { queryHistoryRepository.findByIdOrNull(it.id) } returns it
            }

            // when
            queryHistories.forEach{
                queryHistoryService.deleteQueryHistory(userId, it.id)
            }

            // then
            queryHistories.forEach{
                Assertions.assertThat(it.deletedAt()).isNotNull()
            }
        }

        @Test
        fun `해당 사용자가 검색한 기록이 아니면 에러를 던진다`() {
            // given
            val userId = 1L
            val user = AuthFactory.createKakaoUser()
            val wrongUser = AuthFactory.createKakaoUser()
            wrongUser.id = Long.MIN_VALUE
            val queryHistories = ContentFactory.createQueryHistories(wrongUser)

            BDDMockito.given(userService.getUser(userId))
                .willReturn(user)

            queryHistories.forEach{
                every { queryHistoryRepository.findByIdOrNull(it.id) } returns it
            }

            // when & then
            queryHistories.forEach{
                Assertions.assertThatThrownBy { queryHistoryService.deleteQueryHistory(userId, it.id) }
                    .isInstanceOf(UnavailableDeleteQueryHistoryException::class.java)
            }
        }

        @Test
        fun `사용자가 존재하지 않으면 에러를 던진다`() {
            // given
            val userId = Long.MIN_VALUE

            BDDMockito.given(userService.getUser(userId))
                .willThrow(UserNotFoundException())

            // when & then
            Assertions.assertThatThrownBy { queryHistoryService.deleteQueryHistory(userId, 1L) }
                .isInstanceOf(UserNotFoundException::class.java)
        }

        @Test
        fun `검색 결과가 존재하지 않으면 에러를 던진다`() {
            val userId = 1L
            val user = AuthFactory.createKakaoUser()
            val queryHistoryId = 1L

            BDDMockito.given(userService.getUser(userId))
                .willReturn(user)

            every { queryHistoryRepository.findByIdOrNull(queryHistoryId) } throws NotFoundQueryHistoryException()

            // when & then
            Assertions.assertThatThrownBy { queryHistoryService.deleteQueryHistory(userId, queryHistoryId) }
                .isInstanceOf(NotFoundQueryHistoryException::class.java)
        }
    }
}