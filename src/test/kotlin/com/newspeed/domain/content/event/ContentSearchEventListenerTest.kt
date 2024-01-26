package com.newspeed.domain.content.event

import com.newspeed.domain.category.repository.CategoryRepository
import com.newspeed.domain.content.domain.enums.QueryPlatform
import com.newspeed.domain.content.repository.QueryHistoryRepository
import com.newspeed.domain.user.application.UserService
import com.newspeed.factory.auth.AuthFactory.Companion.createKakaoUser
import com.newspeed.template.UnitTestTemplate
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock

@DisplayName("검색 이벤트가 발생했을 때")
class ContentSearchEventListenerTest: UnitTestTemplate {

    @Mock
    private lateinit var queryHistoryRepository: QueryHistoryRepository

    @Mock
    private lateinit var categoryRepository: CategoryRepository

    @Mock
    private lateinit var userService: UserService

    @InjectMocks
    private lateinit var contentSearchEventListener: ContentSearchEventListener

    @Test
    fun `검색 기록을 저장한다`() {
        // given
        val userId = 1L
        val user = createKakaoUser()
        val event = ContentSearchEvent(
            userId = userId,
            query = "query",
            platform = QueryPlatform.NEWSPEED
        )

        given(userService.getUser(userId))
            .willReturn(user)

        // when
        contentSearchEventListener.handleContentSearchEvent(event)

        // then
        then(queryHistoryRepository).should(times(1)).save(event.toQueryHistory(user))
        then(categoryRepository).should(times(1)).save(event.toCategory(user))
    }
}