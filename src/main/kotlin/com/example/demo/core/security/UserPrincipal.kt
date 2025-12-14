package com.example.demo.core.security

import java.util.UUID

data class UserPrincipal(
    val id: UUID,
    val username: String
)
