package com.newspeed.support

import org.mockito.internal.util.MockUtil
import org.springframework.context.ApplicationContext
import org.springframework.test.context.TestContext
import org.springframework.test.context.TestExecutionListener
import java.util.*

class ResetMockTestExecutionListener: TestExecutionListener {
    private var applicationContextStartupDate: Long = 0
    private val mockCache: MutableList<Any> = ArrayList()

    override fun afterTestMethod(testContext: TestContext) {
        val applicationContext = testContext.applicationContext
        if (mockCache.isEmpty() || isNewContext(applicationContext)) {
            mockCache.clear()
            applicationContextStartupDate = applicationContext.startupDate
            initMocks(applicationContext)
        }
        mockCache.forEach{ MockUtil.resetMock(it) }
    }

    private fun isNewContext(applicationContext: ApplicationContext): Boolean = applicationContext.startupDate != applicationContextStartupDate

    private fun initMocks(applicationContext: ApplicationContext) {
        Arrays.stream(applicationContext.beanDefinitionNames)
            .filter { applicationContext.isSingleton(it) }
            .map { applicationContext.getBean(it) }
            .filter { MockUtil.isMock(it) }
            .forEach { mockCache.add(it) }
    }
}