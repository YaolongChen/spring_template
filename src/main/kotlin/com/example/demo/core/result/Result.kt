package com.example.demo.core.result

data class Result<out T>(val data: T?, val code: ResultCode, val message: String = code.key)