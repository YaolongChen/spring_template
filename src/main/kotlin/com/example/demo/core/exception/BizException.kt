package com.example.demo.core.exception

import com.example.demo.core.result.ResultCode

class BizException(val code: ResultCode, val args: Array<out Any>? = null, override val message: String? = null) :
    RuntimeException(message ?: code.key) {
}