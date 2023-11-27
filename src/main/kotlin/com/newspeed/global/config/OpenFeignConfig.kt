package com.newspeed.global.config

import com.newspeed.global.exception.handler.OpenFeignErrorDecoder
import feign.codec.ErrorDecoder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenFeignConfig(
    private val openFeignErrorDecoder: OpenFeignErrorDecoder
) {
    @Bean
    fun errorDecoder(): ErrorDecoder {
        return openFeignErrorDecoder
    }
}