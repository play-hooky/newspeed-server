package com.newspeed.domain.queryhistory.application

import com.newspeed.domain.auth.domain.enums.LoginPlatform
import com.newspeed.domain.auth.domain.enums.Role
import com.newspeed.domain.content.domain.QueryHistory
import com.newspeed.domain.content.domain.enums.QueryPlatform
import com.newspeed.domain.content.repository.QueryHistoryRepository
import com.newspeed.domain.user.domain.User
import com.newspeed.domain.user.repository.UserRepository
import com.newspeed.factory.auth.AuthFactory
import com.newspeed.factory.auth.AuthFactory.Companion.createKakaoUser
import com.newspeed.global.exception.content.NotFoundQueryHistoryException
import com.newspeed.global.exception.content.UnavailableDeleteQueryHistoryException
import com.newspeed.template.IntegrationTestTemplate
import org.assertj.core.api.Assertions
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@DisplayName("QueryHistory Service 계층 내")
class QueryHistoryServiceIntegrationTest: IntegrationTestTemplate {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var queryHistoryRepository: QueryHistoryRepository

    @Autowired
    private lateinit var queryHistoryService: QueryHistoryService

    private lateinit var user: User

    private lateinit var queryHistory: QueryHistory

    @BeforeEach
    fun setup() {
        user = userRepository.save(
            createKakaoUser()
        )

        queryHistory = queryHistoryRepository.save(
            QueryHistory(
                user = user,
                query = "newspeed",
                platform = QueryPlatform.YOUTUBE
            )
        )
    }

    @Nested
    inner class `검색 이력을 조회할 때` {

        @Test
        fun `DB를 조회하여 결과를 반환한다`() {
            // given
            val userId = user.id

            // when
            val actual = queryHistoryService.getQueryHistory(userId)

            // then
            SoftAssertions.assertSoftly { softly: SoftAssertions ->
                softly.assertThat(actual.queryHistories[0].id).isEqualTo(queryHistory.id)
                softly.assertThat(actual.queryHistories[0].query).isEqualTo(queryHistory.query)
            }
        }
    }

    @Nested
    open inner class `검색어를 추천할 때` {

        @Test
        @Transactional
        open fun `오늘 가장 많이 검색한 검색어를 추천한다`() {
            // given
            val date = LocalDate.now()
            val size = 2
            val mostSearchedQuery = "newspeed"
            val secondSearchedQuery = "hooky"

            queryHistoryRepository.save(
                QueryHistory(
                    user = user,
                    query = mostSearchedQuery,
                    platform = QueryPlatform.YOUTUBE
                )
            )

            queryHistoryRepository.save(
                QueryHistory(
                    user = user,
                    query = secondSearchedQuery,
                    platform = QueryPlatform.YOUTUBE
                )
            )

            queryHistoryRepository.save(
                QueryHistory(
                    user = user,
                    query = mostSearchedQuery,
                    platform = QueryPlatform.YOUTUBE
                )
            )

            // when
            val actual = queryHistoryService.recommendQuery(date, size)

            // then
            SoftAssertions.assertSoftly { softly: SoftAssertions ->
                softly.assertThat(actual.query[0]).isEqualTo(mostSearchedQuery)
                softly.assertThat(actual.query[1]).isEqualTo(secondSearchedQuery)
            }
        }
    }

    @Nested
    inner class `검색 기록을 삭제할 때` {

        @Test
        fun `검색 기록을 조회할 수 없으면 에러를 던진다`() {
            // given
            val userId = user.id
            val queryHistoryId = Long.MIN_VALUE

            // when & then
            Assertions.assertThatThrownBy { queryHistoryService.deleteQueryHistory(userId, queryHistoryId) }
                .isInstanceOf(NotFoundQueryHistoryException::class.java)
        }

        @Test
        fun `사용자 본인의 검색 기록이 아니면 에러를 던진다`() {
            // given
            val wrongUser = userRepository.save(
                User(
                    id = 2L,
                    email = AuthFactory.DUMMY_EMAIL,
                    nickname = AuthFactory.DUMMY_NICKNAME,
                    platform = LoginPlatform.NEWSPEED,
                    profileImageUrl = null,
                    role = Role.USER
                )
            )
            val userId = wrongUser.id
            val queryHistoryId = queryHistory.id

            // when & then
            Assertions.assertThatThrownBy { queryHistoryService.deleteQueryHistory(userId, queryHistoryId) }
                .isInstanceOf(UnavailableDeleteQueryHistoryException::class.java)
        }

        @Test
        fun `DB를 조회하여 삭제한다`() {
            // given
            val userId = user.id
            val queryHistoryId = queryHistory.id

            // when
            queryHistoryService.deleteQueryHistory(userId, queryHistoryId)

            // then
            Assertions.assertThat(queryHistoryRepository.findByIdOrNull(queryHistoryId)).isNull()
        }
    }
}