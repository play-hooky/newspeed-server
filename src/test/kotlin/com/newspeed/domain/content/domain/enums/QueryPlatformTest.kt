package com.newspeed.domain.content.domain.enums

import com.newspeed.template.UnitTestTemplate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("컨텐츠 검색 플랫폼 enum 에서")
class QueryPlatformTest: UnitTestTemplate {

    @Nested
    inner class `url로 플랫폼을 조회할 때` {

        @Test
        fun `url에 포함된 문자열로 조회한다`() {
            // given
            val url = "https://www.youtube.com/watch?v=Kjq10TNm14k"
            val expected = QueryPlatform.YOUTUBE

            // when
            val actual = QueryPlatform.findByUrl(url)

            // then
            assertThat(actual).isEqualTo(expected)
        }

        @Test
        fun `조회할 수 없으면 디폴트 값으로 조회한다`() {
            // given
            val url = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS7pj7a-pFI1ZzMEykUHgQScPrW-AqAtx_72KfWtEUniQ&s"
            val expected = QueryPlatform.NEWSPEED

            // when
            val actual = QueryPlatform.findByUrl(url)

            // then
            assertThat(actual).isEqualTo(expected)
        }
    }
}