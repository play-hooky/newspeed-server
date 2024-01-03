package com.newspeed.domain.content.repository

import com.newspeed.domain.content.domain.QueryHistory
import com.newspeed.domain.content.domain.enums.QueryPlatform
import com.newspeed.domain.content.dto.RecommendQueryDTO
import com.newspeed.domain.user.repository.UserRepository
import com.newspeed.factory.auth.AuthFactory.Companion.createKakaoUser
import com.newspeed.template.RepositoryTestTemplate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate

@DisplayName("검색 기록 관리 repository 계층에서 ")
@RepositoryTestTemplate
class QueryHistoryCustomRepositoryImplTest {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var queryHistoryRepository: QueryHistoryRepository

    @Test
    fun `오늘 가장 많이 검색한 기록을 조회한다`() {
        // given
        val date = LocalDate.now()
        val size = 5
        val query = "newspeed"
        prepareQueryHistories(size, query)

        val expected = MutableList(size) {
            RecommendQueryDTO(
                query = "${query}${it}",
                count = size - it.toLong() + 1
            )
        }

        // when
        val actual = queryHistoryRepository.findDailyMaxQueryHistory(date, size)

        // then
        assertThat(expected).usingRecursiveComparison().isEqualTo(actual)
    }

    private fun prepareQueryHistories(
        size: Int,
        query: String
    ) {
        val user = userRepository.save(
            createKakaoUser()
        )

        for (i in 0..size) {
            for (j in i..size) {
                queryHistoryRepository.save(
                    QueryHistory(
                        user = user,
                        query = "${query}${i}",
                        platform = QueryPlatform.NEWSPEED
                    )
                )
            }
        }
    }
}