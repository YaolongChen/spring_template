package com.example.demo.auth.domain.model

import java.util.UUID

data class User(
    val id: UUID, val account: String, val password: String, val nickname: String
) {
    companion object {
        fun create(
            account: String, password: String, nickname: String = UUID.randomUUID().toString()
        ): User {
            return User(UUID.randomUUID(), account, password, nickname)
        }
    }
}