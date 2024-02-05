package com.newspeed.template

import com.newspeed.factory.global.MockAllServiceBeanFactoryPostProcessor
import com.newspeed.factory.global.TestAuthConfig
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import

@WebMvcTest
@Retention(AnnotationRetention.RUNTIME)
@Import(value = [
    MockAllServiceBeanFactoryPostProcessor::class,
    TestAuthConfig::class
])
annotation class CustomWebMvcTest