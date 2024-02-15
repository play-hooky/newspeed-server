package com.newspeed.support

import com.newspeed.domain.auth.domain.AuthPayload
import com.newspeed.domain.auth.domain.AuthenticateContext
import com.newspeed.domain.auth.domain.enums.LoginPlatform
import com.newspeed.domain.auth.domain.enums.Role
import com.newspeed.factory.auth.AuthFactory.Companion.DUMMY_EMAIL
import com.newspeed.util.WithMockAuth
import org.springframework.test.context.TestContext
import org.springframework.test.context.TestExecutionListener

class MockAuthTestExecutionListener: TestExecutionListener {
    override fun beforeTestMethod(
        testContext: TestContext
    ) {
        val testMethod = testContext.testMethod
        if (testMethod.isAnnotationPresent(WithMockAuth::class.java)) {
            val withMockAuth: WithMockAuth = testMethod.getDeclaredAnnotation(WithMockAuth::class.java)
            val applicationContext = testContext.applicationContext
            val authenticateContext = applicationContext.getBean("authenticateContext") as AuthenticateContext
            authenticateContext.setAuthenticate(AuthPayload(
                userId = withMockAuth.userId,
                role = withMockAuth.role,
                loginPlatform = LoginPlatform.NEWSPEED,
                email = DUMMY_EMAIL
            ))
        }
    }

    override fun afterTestMethod(
        testContext: TestContext
    ) {
        val applicationContext = testContext.applicationContext
        val authenticateContext = applicationContext.getBean("authenticateContext") as AuthenticateContext

        authenticateContext.setAuthenticate(AuthPayload(
            userId = Long.MIN_VALUE,
            role = Role.ANONYMOUS,
            loginPlatform = LoginPlatform.NEWSPEED,
            email = DUMMY_EMAIL
        ))
    }
}