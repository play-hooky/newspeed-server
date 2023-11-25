package com.newspeed.user.global.exception.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.newspeed.user.global.exception.feign.FeignException
import com.newspeed.user.global.exception.model.ExceptionType
import feign.Response
import feign.codec.ErrorDecoder
import feign.codec.StringDecoder
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class OpenFeignErrorDecoder(
    private val objectMapper: ObjectMapper
): ErrorDecoder {

    companion object {
        private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)
        private val stringDecoder = StringDecoder()
    }

    override fun decode(
        methodKey: String?,
        response: Response
    ): Exception {
        val errorResponseAsString = getErrorResponseAsString(response)
        log.error(errorResponseAsString)
        return FeignException(response.reason())
    }

    private fun getErrorResponseAsString(
        response: Response
    ): String =
        runCatching {
            return stringDecoder.decode(response, String::class.java).toString()
        }
        .onFailure {
            return response.reason()
        }
        .getOrNull() ?: ExceptionType.INTERNAL_SERVER_EXCEPTION.message
}