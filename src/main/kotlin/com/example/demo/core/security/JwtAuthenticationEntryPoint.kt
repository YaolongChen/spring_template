package com.example.demo.core.security

import com.example.demo.core.result.Result
import com.example.demo.core.result.ResultCode
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationEntryPoint(private val objectMapper: ObjectMapper, private val messageSource: MessageSource) :
    AuthenticationEntryPoint {

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException?
    ) {
        // 1. 设置响应头
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8"

        // 2. 获取我们在 Filter 中设置的错误信息 (如果有的话)
        // 如果是从 Filter 过来的，getAttribute("jwt_error") 会有值
        // 如果是纯粹没带 Token，则是 authException 的默认信息
        val errorCode = (request.getAttribute("jwt_error") as? ResultCode) ?: ResultCode.ERROR
        val locale = LocaleContextHolder.getLocale()
        val translatedMessage = messageSource.getMessage(
            errorCode.key, null, errorCode.key, locale
        ) ?: ""

        // 3. 构建错误 JSON
        val result = Result(
            code = errorCode,
            message = translatedMessage,
            data = null
        )

        // 4. 写入响应流
        response.writer.write(objectMapper.writeValueAsString(result))
    }
}