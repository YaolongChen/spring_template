package com.example.demo.core.result

import com.fasterxml.jackson.annotation.JsonValue

enum class ResultCode(val code: Int, val key: String) {
    SUCCESS(200, "response.success"),
    ERROR(400, "response.error"),

    TOKEN_EXPIRED(10000, "token.expired"),

    USER_REGISTER_EXISTED(40000, "user.register.existed"),
    USER_LOGIN_ERROR(40001, "user.login.error"),
    USER_NOT_FOUND(40002, "user.notfound.error");

    @JsonValue
    fun getCodeValue() = code
}