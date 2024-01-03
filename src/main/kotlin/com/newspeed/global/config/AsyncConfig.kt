package com.newspeed.global.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

@EnableAsync
@Configuration
class AsyncConfig {
    companion object {
        private const val THREAD_NAME_PREFIX = "async-executor-"
        private const val POOL_SIZE = 10
    }

    @Bean(name = ["asyncTaskExecutor"])
    fun taskExecutor(): ThreadPoolTaskExecutor {
        val taskExecutor = ThreadPoolTaskExecutor()
        taskExecutor.setThreadNamePrefix(THREAD_NAME_PREFIX)
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true)
        taskExecutor.corePoolSize = POOL_SIZE
        taskExecutor.maxPoolSize = POOL_SIZE * 2
        taskExecutor.queueCapacity = POOL_SIZE * 5
        taskExecutor.keepAliveSeconds = 30

        return taskExecutor
    }
}