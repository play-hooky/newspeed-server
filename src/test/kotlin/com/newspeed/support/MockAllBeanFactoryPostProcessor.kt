package com.newspeed.support

import org.junit.platform.commons.util.ClassFilter
import org.junit.platform.commons.util.ReflectionUtils
import org.mockito.Mockito
import org.springframework.beans.factory.config.BeanFactoryPostProcessor
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

class MockAllBeanFactoryPostProcessor: BeanFactoryPostProcessor {
    override fun postProcessBeanFactory(
        beanFactory: ConfigurableListableBeanFactory
    ) {
        val registry = beanFactory as BeanDefinitionRegistry
        val classFilter = ClassFilter.of {
            it.isAnnotationPresent(Service::class.java) or
            it.isAnnotationPresent(Component::class.java) or
            it.isAnnotationPresent(FeignClient::class.java)
        }
        ReflectionUtils
            .findAllClassesInPackage("com.newspeed", classFilter)
            .forEach{
                val bean = BeanDefinitionBuilder.genericBeanDefinition(it).beanDefinition
                registry.registerBeanDefinition(it.simpleName, bean)
                beanFactory.registerSingleton(it.simpleName, Mockito.mock(it))
            }
    }
}