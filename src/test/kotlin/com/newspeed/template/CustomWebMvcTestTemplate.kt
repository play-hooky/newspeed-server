package com.newspeed.template

import com.newspeed.config.TestAuthConfig
import com.newspeed.support.MockAllBeanFactoryPostProcessor
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import

@WebMvcTest
@Import(value = [
    MockAllBeanFactoryPostProcessor::class,
    TestAuthConfig::class
])
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@AutoConfigureMockMvc(addFilters = false)
annotation class CustomWebMvcTestTemplate