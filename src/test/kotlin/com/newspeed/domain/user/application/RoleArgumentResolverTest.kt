package com.newspeed.domain.user.application

import com.newspeed.domain.auth.domain.AuthenticateContext
import com.newspeed.domain.auth.domain.enums.Role
import com.newspeed.factory.auth.AuthFactory.Companion.createAuthPayload
import com.newspeed.global.exception.auth.NotEnoughPermissionException
import com.newspeed.global.exception.model.ExceptionType
import com.newspeed.template.UnitTestTemplate
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@DisplayName("RoleArgumentResolver 에서")
class RoleArgumentResolverTest: UnitTestTemplate {
    private lateinit var authenticateContext: AuthenticateContext
    private lateinit var roleArgumentResolver: RoleArgumentResolver

    @BeforeEach
    fun setup() {
        authenticateContext = AuthenticateContext()
        roleArgumentResolver = RoleArgumentResolver(
            role = Role.USER,
            authenticateContext = authenticateContext
        )
    }

    @Nested
    inner class resolveArgument_메서드는  {

        @ParameterizedTest
        @ValueSource(strings = ["ANONYMOUS", "ADMIN"])
        fun `Role이 User일 때 User가 아니면 예외를 던진다`(
            role: Role
        ) {
            // given
            val authPayload = createAuthPayload(role)
            authenticateContext.setAuthenticate(authPayload)

            // when & then
            assertThatThrownBy { roleArgumentResolver.resolveArgument(null, null, null, null) }
                .isInstanceOf(NotEnoughPermissionException::class.java)
                .hasMessage(ExceptionType.NOT_ENOUGH_PERMISSION_EXCEPTION.message)
        }

        @Test
        fun `Role이 User일 때 User이면 userId를 반환한다`() {
            // given
            val authPayload = createAuthPayload(Role.USER)
            authenticateContext.setAuthenticate(authPayload)

            // when
            val userId = roleArgumentResolver.resolveArgument(null, null, null, null)

            // then
            assertThat(userId).isEqualTo(authenticateContext.userId())
        }
    }

}