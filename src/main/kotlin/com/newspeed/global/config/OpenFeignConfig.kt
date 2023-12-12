package com.newspeed.global.config

import com.newspeed.global.exception.handler.OpenFeignErrorDecoder
import feign.codec.ErrorDecoder
import org.springframework.cloud.openfeign.FeignFormatterRegistrar
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar


@Configuration
class OpenFeignConfig(
    private val openFeignErrorDecoder: OpenFeignErrorDecoder
) {

    @Bean
    fun errorDecoder(): ErrorDecoder {
        return openFeignErrorDecoder
    }

    @Bean
    fun localDateFeignFormatterRegister(): FeignFormatterRegistrar {
        return FeignFormatterRegistrar {
            registry  ->
            val registrar = DateTimeFormatterRegistrar()
            registrar.setUseIsoFormat(true)
            registrar.registerFormatters(registry)
        }
    }
}