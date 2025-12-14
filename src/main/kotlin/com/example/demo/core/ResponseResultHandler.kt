package com.example.demo.core

import com.example.demo.core.exception.BizException
import com.example.demo.core.result.Result
import com.example.demo.core.result.ResultCode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

@RestControllerAdvice
class ResponseResultHandler(val objectMapper: ObjectMapper, val messageSource: MessageSource) :
    ResponseBodyAdvice<Any> {

    override fun supports(returnType: MethodParameter, converterType: Class<out HttpMessageConverter<*>>): Boolean {
        return true
    }

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ): Any? {
        if (body is Result<*>) {
            return body
        }

        val locale = LocaleContextHolder.getLocale()
        val translatedMessage = messageSource.getMessage(
            ResultCode.SUCCESS.key, null, ResultCode.SUCCESS.key, locale
        )

        val result = Result(body, ResultCode.SUCCESS, translatedMessage ?: "")

        if (body is String) {
            response.headers.contentType = MediaType.APPLICATION_JSON
            return objectMapper.writeValueAsString(result)
        }

        return result
    }

    @ExceptionHandler(BizException::class)
    fun handleBizException(bizException: BizException): Result<Nothing?> {
        val locale = LocaleContextHolder.getLocale()
        val translatedMessage = messageSource.getMessage(
            bizException.code.key, bizException.args, bizException.code.key, locale
        ) ?: bizException.message

        return Result(
            null, bizException.code, translatedMessage ?: ""
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(e: Exception): Result<Nothing?> {
        val locale = LocaleContextHolder.getLocale()
        val translatedMessage = messageSource.getMessage(
            ResultCode.ERROR.key, null, ResultCode.ERROR.key, locale
        )

        return Result(null, ResultCode.ERROR, translatedMessage ?: "")
    }
}