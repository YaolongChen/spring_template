package com.example.demo.auth.interfaces

import com.example.demo.auth.domain.command.LoginByAccountCommand
import com.example.demo.auth.domain.command.RegisterByAccountCommand
import com.example.demo.auth.domain.service.AuthService
import com.example.demo.auth.interfaces.dto.LoginByAccountRequest
import com.example.demo.auth.interfaces.dto.RegisterByAccountRequest
import com.example.demo.core.security.JwtUtil
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(private val authService: AuthService, private val jwtUtil: JwtUtil) {
    @PostMapping("/registerByAccount")
    fun registerByAccount(@RequestBody dto: RegisterByAccountRequest) {
        val command = RegisterByAccountCommand(dto.account, dto.password)
        authService.registerByAccount(command)
    }

    @PostMapping("/loginByAccount")
    fun loginByAccount(@RequestBody dto: LoginByAccountRequest): String {
        val command = LoginByAccountCommand(dto.account, dto.password)
        val user = authService.loginByAccount(command)

        val token = jwtUtil.generateToken(
            id = user.id.toString(),
            username = user.account
        )
        return token
    }
}