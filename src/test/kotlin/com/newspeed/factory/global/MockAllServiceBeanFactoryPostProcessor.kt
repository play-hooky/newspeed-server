package com.newspeed.factory.global

import org.junit.platform.commons.util.ClassFilter
import org.junit.platform.commons.util.ReflectionUtils
import org.mockito.Mockito
import org.springframework.beans.factory.config.BeanFactoryPostProcessor
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.stereotype.Service
import java.util.function.Consumer

class MockAllServiceBeanFactoryPostProcessor: BeanFactoryPostProcessor {
    override fun postProcessBeanFactory(
        beanFactory: ConfigurableListableBeanFactory
    ) {
        val registry = beanFactory as BeanDefinitionRegistry
        val classFilter = ClassFilter.of {
            clazz -> clazz.isAnnotationPresent(Service::class.java)
        }
        ReflectionUtils
            .findAllClassesInPackage("com.newspeed", classFilter)
            .forEach(
                Consumer { clazz ->
                    val bean = BeanDefinitionBuilder.genericBeanDefinition(clazz).beanDefinition
                    registry.registerBeanDefinition(clazz.simpleName, bean)
                    beanFactory.registerSingleton(clazz.simpleName, Mockito.mock(clazz))
                }
            )
    }
}