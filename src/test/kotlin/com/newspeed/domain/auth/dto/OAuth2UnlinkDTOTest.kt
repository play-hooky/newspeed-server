package com.newspeed.domain.auth.dto

import com.newspeed.global.exception.auth.DifferentUserException
import com.newspeed.template.UnitTestTemplate
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("회원 탈퇴를 위한 객체 DTO에서 ")
class OAuth2UnlinkDTOTest: UnitTestTemplate {

    @Nested
    inner class `이메일로 검증할 때` {

        @Test
        fun `저장된 이메일과 oauth로 얻은 이메일이 같으면 통과한다` (){
            // given
            val email = "play-hooky@gmail.com"
            val oAuth2UnlinkDTO = OAuth2UnlinkDTO(
                authorizationCode = "hooky",
                email = email
            )

            // when & then
            Assertions.assertThatCode{ oAuth2UnlinkDTO.validateByEmail(email) }
                .doesNotThrowAnyException()
        }

        @Test
        fun `저장된 이메일과 oauth로 얻은 이메일이 다르면 에러를 던진다` (){
            // given
            val email = "play-hooky@gmail.com"
            val wrongEmail = "wrong-${email}"
            val oAuth2UnlinkDTO = OAuth2UnlinkDTO(
                authorizationCode = "hooky",
                email = email
            )

            // when & then
            Assertions.assertThatThrownBy { oAuth2UnlinkDTO.validateByEmail(wrongEmail) }
                .isInstanceOf(DifferentUserException::class.java)
        }
    }
}