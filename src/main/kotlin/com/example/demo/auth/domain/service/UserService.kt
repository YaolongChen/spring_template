package com.example.demo.auth.domain.service

import com.example.demo.auth.domain.command.GetUserProfileCommand
import com.example.demo.auth.domain.model.User
import com.example.demo.auth.domain.repository.UserRepository
import com.example.demo.core.exception.BizException
import com.example.demo.core.result.ResultCode
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {
    fun getUserProfile(command: GetUserProfileCommand): User {
        val user = userRepository.findUserById(command.id)

        if (user == null) {
            throw BizException(code = ResultCode.USER_NOT_FOUND)
        }

        return user
    }
}