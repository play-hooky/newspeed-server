package com.newspeed.domain.jwt.application

import com.newspeed.global.exception.auth.IllegalAuthorizationException
import com.newspeed.global.exception.auth.NotFoundTokenException
import com.newspeed.template.UnitTestTemplate
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.NullSource
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.springframework.http.HttpHeaders
import javax.servlet.http.HttpServletRequest

@DisplayName("HeaderTokenExtractor에서 ")
class HeaderTokenExtractorTest: UnitTestTemplate {
    @Mock
    private lateinit var request: HttpServletRequest

    private val headerTokenExtractor = HeaderTokenExtractor()

    @Nested
    inner class `Authorization token 추출 로직은 ` {

        @ParameterizedTest
        @NullSource
        @ValueSource(strings = ["", " "])
        fun `http header 내 Authorization 헤더가 존재하지 않으면 예외를 던진다`(
            token: String?
        ) {
            // given
            given(request.getHeader(HttpHeaders.AUTHORIZATION))
                .willReturn(token)

            // when & then
            assertThatThrownBy { headerTokenExtractor.extract(request) }
                .isInstanceOf(Exception::class.java)
        }

        @Test
        fun `Bearer 토큰이 아니면 예외를 던진다`() {
            // given
            given(request.getHeader(HttpHeaders.AUTHORIZATION))
                .willReturn("Bear playhooky")

            // when & then
            assertThatThrownBy { headerTokenExtractor.extract(request) }
                .isInstanceOf(IllegalAuthorizationException::class.java)
        }

        @Test
        fun `Bearer 형식에 토큰이 존재하지 않으면 예외를 던진다`() {
            // given
            given(request.getHeader(HttpHeaders.AUTHORIZATION))
                .willReturn("Bearer ")

            // when & then
            assertThatThrownBy { headerTokenExtractor.extract(request) }
                .isInstanceOf(NotFoundTokenException::class.java)
        }

        @Test
        fun `Bearer 형식에 토큰이 존재하면 토큰을 반환한다`() {
            // given
            val expected = "playhooky"
            given(request.getHeader(HttpHeaders.AUTHORIZATION))
                .willReturn("Bearer $expected")

            // when
            val actual = headerTokenExtractor.extract(request)

            // then
            assertThat(expected).isEqualTo(actual)
        }
    }

}