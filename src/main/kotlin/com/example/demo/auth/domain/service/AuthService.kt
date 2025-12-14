package com.example.demo.auth.domain.service

import com.example.demo.auth.domain.command.LoginByAccountCommand
import com.example.demo.auth.domain.command.RegisterByAccountCommand
import com.example.demo.auth.domain.model.User
import com.example.demo.auth.domain.repository.UserRepository
import com.example.demo.core.exception.BizException
import com.example.demo.core.result.ResultCode
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val passwordEncoder: PasswordEncoder, private val userRepository: UserRepository
) {

    @Transactional
    fun registerByAccount(command: RegisterByAccountCommand): User {
        val user = userRepository.findUserByAccount(command.account)
        if (user != null) {
            throw BizException(ResultCode.USER_REGISTER_EXISTED)
        }

        val encodedPassword = passwordEncoder.encode(command.password)
        val newUser = User.create(command.account, encodedPassword)

        return userRepository.createUser(newUser)
    }

    fun loginByAccount(command: LoginByAccountCommand): User {
        val user = userRepository.findUserByAccount(command.account)

        val encodedPassword = passwordEncoder.encode(command.password)
        if (user == null || !passwordEncoder.matches(command.password, encodedPassword)) {
            throw BizException(ResultCode.USER_LOGIN_ERROR)
        }

        return user
    }
}