package com.newspeed.domain.content.converter

import com.newspeed.template.UnitTestTemplate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

@DisplayName("Youtube 검색 필드 converter 객체에서 ")
class YoutubeValueConverterKtTest: UnitTestTemplate {

    @Nested
    inner class `Youtube 시간을 표현할 때` {

        @Test
        fun `1분전이면 초로 표현한다`() {
            // given
            val day = LocalDateTime.now().minusSeconds(58)
            val expected = "58초 전"

            // when
            val actual = day.toYouTubeTimeDifference()

            assertThat(expected).isEqualTo(actual)
        }

        @Test
        fun `1시간전이면 분으로 표현한다`() {
            // given
            val day = LocalDateTime.now().minusMinutes(40)
            val expected = "40분 전"

            // when
            val actual = day.toYouTubeTimeDifference()

            assertThat(expected).isEqualTo(actual)
        }

        @Test
        fun `1일전이면 시간으로 표현한다`() {
            // given
            val day = LocalDateTime.now().minusHours(3)
            val expected = "3시간 전"

            // when
            val actual = day.toYouTubeTimeDifference()

            assertThat(expected).isEqualTo(actual)
        }

        @Test
        fun `1달전이면 일로 표현한다`() {
            // given
            val day = LocalDateTime.now().minusDays(25)
            val expected = "25일 전"

            // when
            val actual = day.toYouTubeTimeDifference()

            assertThat(expected).isEqualTo(actual)
        }

        @Test
        fun `1년전이면 개월로 표현한다`() {
            // given
            val day = LocalDateTime.now().minusMonths(5)
            val expected = "5개월 전"

            // when
            val actual = day.toYouTubeTimeDifference()

            assertThat(expected).isEqualTo(actual)
        }

        @Test
        fun `100년전이면 년으로 표현한다`() {
            // given
            val day = LocalDateTime.now().minusYears(3)
            val expected = "3년 전"

            // when
            val actual = day.toYouTubeTimeDifference()

            assertThat(expected).isEqualTo(actual)
        }

        @Test
        fun `표현할 수 없으면 특정 날짜로 표현한다`() {
            // given
            val day = LocalDateTime.now().minusYears(101)
            val expected = "${day.year}년 ${day.month.value}월 ${day.dayOfMonth}일"

            // when
            val actual = day.toYouTubeTimeDifference()

            assertThat(expected).isEqualTo(actual)
        }
    }

    @Nested
    inner class `Youtube 조회수를 표현할 때` {

        @Test
        fun `조회수가 없으면 0으로 표현한다`() {
            // given
            val view = "0"
            val expected = "0회"

            // when
            val actual = view.toYoutubeView()

            // then
            assertThat(expected).isEqualTo(actual)
        }

        @Test
        fun `조회수가 천 미만이면 숫자로 표현한다`() {
            // given
            val view = "123"
            val expected = "123회"

            // when
            val actual = view.toYoutubeView()

            // then
            assertThat(expected).isEqualTo(actual)
        }

        @Test
        fun `조회수가 천 이상 만 미만이고 천의 배수이면 ~천회로 표현한다`() {
            // given
            val view = "3000"
            val expected = "3천회"

            // when
            val actual = view.toYoutubeView()

            // then
            assertThat(expected).isEqualTo(actual)
        }

        @Test
        fun `조회수가 천 이상 만 미만이고 천의 배수가 아니면 소수점과 함께 ~천회로 표현한다`() {
            // given
            val view = "3123"
            val expected = "3.1천회"

            // when
            val actual = view.toYoutubeView()

            // then
            assertThat(expected).isEqualTo(actual)
        }

        @Test
        fun `조회수가 만 이상 십만미만이고 만의 배수가 이면 ~만회로 표현한다`() {
            // given
            val view = "20000"
            val expected = "2만회"

            // when
            val actual = view.toYoutubeView()

            // then
            assertThat(expected).isEqualTo(actual)
        }

        @Test
        fun `조회수가 만 이상 천만 미만이고 만의 배수가 아니면 ~만회로 표현한다`() {
            // given
            val view = "5123132"
            val expected = "512만회"

            // when
            val actual = view.toYoutubeView()

            // then
            assertThat(expected).isEqualTo(actual)
        }

        @Test
        fun `조회수가 억 이상 조 미만이고 억의 배수이면 ~억회로 표현한다`() {
            // given
            val view = "100000000"
            val expected = "1억회"

            // when
            val actual = view.toYoutubeView()

            // then
            assertThat(expected).isEqualTo(actual)
        }

        @Test
        fun `조회수가 억 이상 백억 미만이고 억의 배수가 아니면 ~억회로 표현한다`() {
            // given
            val view = "5320320000"
            val expected = "53억회"

            // when
            val actual = view.toYoutubeView()

            // then
            assertThat(expected).isEqualTo(actual)
        }
    }
}