package com.newspeed.domain.user.application

import com.newspeed.domain.user.repository.UserRepository
import com.newspeed.factory.auth.AuthFactory
import com.newspeed.template.UnitTestTemplate
import org.assertj.core.api.SoftAssertions
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.any
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock

@DisplayName("사용자 서비스 계층에서 ")
class UserServiceTest: UnitTestTemplate {

    @Mock
    private lateinit var userRepository: UserRepository

    @InjectMocks
    private lateinit var userService: UserService

    @Nested
    inner class OAuth2_사용자_정보가 {

        @Test
        fun 기존_사용자면_기존_정보를_반환한다() {
            // given
            val kakaoOAuth2 = AuthFactory.createKakaoOAuth2User()
            val expected = AuthFactory.createKakaoUser()

            given(userRepository.findByPlatformAndEmail(kakaoOAuth2.platform, kakaoOAuth2.email))
                .willReturn(expected)

            // when
            val actual = userService.saveIfNotExist(kakaoOAuth2)

            // then
            assertSoftly { softly: SoftAssertions ->
                softly.assertThat(actual.email).isEqualTo(expected.email)
                softly.assertThat(actual.nickname).isEqualTo(expected.nickname)
                softly.assertThat(actual.platform).isEqualTo(expected.platform)
                softly.assertThat(actual.profileImageUrl).isEqualTo(expected.profileImageUrl)
                softly.assertThat(actual.role).isEqualTo(expected.role)
            }
        }

        @Test
        fun 신규_사용자면_DB에_저장한다() {
            // given
            val kakaoOAuth2 = AuthFactory.createKakaoOAuth2User()
            val expected = AuthFactory.createKakaoUser()

            given(userRepository.findByPlatformAndEmail(kakaoOAuth2.platform, kakaoOAuth2.email))
                .willReturn(null)

            given(userRepository.save(any()))
                .willReturn(expected)

            // when
            val actual = userService.saveIfNotExist(kakaoOAuth2)

            // then
            assertSoftly { softly: SoftAssertions ->
                softly.assertThat(actual.email).isEqualTo(expected.email)
                softly.assertThat(actual.nickname).isEqualTo(expected.nickname)
                softly.assertThat(actual.platform).isEqualTo(expected.platform)
                softly.assertThat(actual.profileImageUrl).isEqualTo(expected.profileImageUrl)
                softly.assertThat(actual.role).isEqualTo(expected.role)
            }
        }
    }

}