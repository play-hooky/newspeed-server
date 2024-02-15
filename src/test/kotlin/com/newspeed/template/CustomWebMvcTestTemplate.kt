package com.newspeed.template

import com.newspeed.config.TestAuthConfig
import com.newspeed.support.MockAllBeanFactoryPostProcessor
import com.newspeed.support.MockAuthTestExecutionListener
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.TestExecutionListeners

@WebMvcTest
@Import(value = [
    MockAllBeanFactoryPostProcessor::class,
    TestAuthConfig::class
])
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@AutoConfigureMockMvc(addFilters = false)
@TestExecutionListeners(
    value = [MockAuthTestExecutionListener::class],
    mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
annotation class CustomWebMvcTestTemplate