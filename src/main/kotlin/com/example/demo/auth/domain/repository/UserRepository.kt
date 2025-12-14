package com.example.demo.auth.domain.repository

import com.example.demo.auth.domain.model.User
import java.util.UUID

interface UserRepository {
    fun findUserByAccount(account: String): User?
    fun createUser(user: User): User
    fun findUserById(id: UUID): User?
}