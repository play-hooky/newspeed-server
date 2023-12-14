package com.newspeed.global.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.newspeed.global.serde.LocalDateTimeSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDateTime


@Configuration
class JacksonConfig {

    @Bean
    fun objectMapper(
        localDateTimeSerde: SimpleModule
    ): ObjectMapper = ObjectMapper()
        .registerModule(JavaTimeModule())
        .registerModule(localDateTimeSerde)
        .registerKotlinModule()

    @Bean
    fun localDateTimeSerde(): SimpleModule {
        val localDateTimeSerde = SimpleModule()
        localDateTimeSerde.addSerializer(LocalDateTime::class.java, LocalDateTimeSerializer())

        return localDateTimeSerde
    }
}