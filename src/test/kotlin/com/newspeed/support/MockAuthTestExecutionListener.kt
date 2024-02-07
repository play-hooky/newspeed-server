package com.newspeed.support

import com.newspeed.domain.auth.domain.AuthPayload
import com.newspeed.domain.auth.domain.AuthenticateContext
import com.newspeed.domain.auth.domain.enums.LoginPlatform
import com.newspeed.domain.auth.domain.enums.Role
import com.newspeed.factory.auth.AuthFactory.Companion.DUMMY_EMAIL
import org.springframework.test.context.TestContext
import org.springframework.test.context.TestExecutionListener

class MockAuthTestExecutionListener: TestExecutionListener {
    override fun beforeTestMethod(
        testContext: TestContext
    ) {
        val testMethod = testContext.testMethod
        if (testMethod.isAnnotationPresent(MockUser::class.java)) {
            val mockUser = testMethod.getDeclaredAnnotation(MockUser::class.java)
            getAuthenticationContext(testContext)
                .setAuthenticate(
                    AuthPayload(
                        userId = mockUser.id,
                        role = mockUser.role,
                        loginPlatform = LoginPlatform.NEWSPEED,
                        email = DUMMY_EMAIL
                )
            )
        }
    }

    override fun afterTestMethod(
        testContext: TestContext
    ) {
        getAuthenticationContext(testContext)
            .setAuthenticate(
            AuthPayload(
                userId = Long.MIN_VALUE,
                role = Role.ANONYMOUS,
                loginPlatform = LoginPlatform.NEWSPEED,
                email = DUMMY_EMAIL
            )
        )
    }

    private fun getAuthenticationContext(
        testContext: TestContext
    ): AuthenticateContext {
        val auth = testContext.applicationContext.getBean(AuthenticateContext::class.java)

        return auth
    }
}