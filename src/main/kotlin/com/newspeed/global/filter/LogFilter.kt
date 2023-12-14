package com.newspeed.global.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.stream.JsonReader
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import org.springframework.web.util.WebUtils
import java.io.IOException
import java.io.StringReader
import java.nio.charset.StandardCharsets
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class LogFilter(
    private val objectMapper: ObjectMapper
): OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val requestWrapper = ContentCachingRequestWrapper(request)
        val responseWrapper = ContentCachingResponseWrapper(response)

        val start = System.currentTimeMillis()
        filterChain.doFilter(requestWrapper, responseWrapper)
        val end = System.currentTimeMillis()

        val method = requestWrapper.method
        val requestURI = if (requestWrapper.queryString == null) requestWrapper.requestURI else requestWrapper.requestURI + requestWrapper.queryString
        val status = responseWrapper.status
        val time = (end - start) / 1000.0
        val headers = getHeaders(requestWrapper)
        val requestBody = getRequestBody(requestWrapper)
        val responseBody = getResponseBody(responseWrapper)

        logger.info(
            """
                [REQUEST] $method - $requestURI $status - ${time}s
                Headers : $headers
                Request : $requestBody
                Response : $responseBody
            """.trimIndent()
        )
    }

    private fun getHeaders(
        request: HttpServletRequest
    ): String {
        val header: MutableMap<Any, Any> = HashMap()
        val headers = request.headerNames

        while (headers.hasMoreElements()) {
            val headerName = headers.nextElement()
            header[headerName] = request.getHeader(headerName)
        }

        return GsonBuilder()
            .setPrettyPrinting()
            .create()
            .toJson(header)
    }

    @Throws(IOException::class)
    private fun getRequestBody(request: ContentCachingRequestWrapper): String {
        val wrapper = WebUtils.getNativeRequest(
            request,
            ContentCachingRequestWrapper::class.java
        )
        if (wrapper != null) {
            val buf = wrapper.contentAsByteArray
            if (buf.isNotEmpty()) {
                return getPrettyJson(buf)
            }
        }
        return " - "
    }

    @Throws(IOException::class)
    private fun getResponseBody(response: HttpServletResponse): String {
        var payload: String? = null
        val wrapper = WebUtils.getNativeResponse(
            response,
            ContentCachingResponseWrapper::class.java
        )
        if (wrapper != null) {
            val buf = wrapper.contentAsByteArray
            if (buf.isNotEmpty()) {
                payload = getPrettyJson(buf)
                wrapper.copyBodyToResponse()
            }
        }
        return payload ?: " - "
    }

    @Throws(IOException::class)
    private fun getPrettyJson(buf: ByteArray): String {
        val jsonReader = JsonReader(StringReader(String(buf, 0, buf.size, StandardCharsets.UTF_8)))
        jsonReader.isLenient = true

        return objectMapper
            .writerWithDefaultPrettyPrinter()
            .writeValueAsString(Gson().fromJson(jsonReader, Any::class.java))
    }
}