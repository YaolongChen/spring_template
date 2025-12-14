package com.example.demo.auth.interfaces

import com.example.demo.auth.domain.command.GetUserProfileCommand
import com.example.demo.auth.domain.service.UserService
import com.example.demo.auth.interfaces.dto.GetMyProfileResponse
import com.example.demo.core.security.UserPrincipal
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(val userService: UserService) {
    @GetMapping("/me")
    fun getMyProfile(@AuthenticationPrincipal userPrincipal: UserPrincipal): GetMyProfileResponse {
        val user = userService.getUserProfile(GetUserProfileCommand(id = userPrincipal.id))

        return GetMyProfileResponse(account = user.account, nickname = user.nickname)
    }
}