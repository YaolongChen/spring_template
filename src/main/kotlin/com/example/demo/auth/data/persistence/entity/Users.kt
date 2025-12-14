package com.example.demo.auth.data.persistence.entity

import org.jetbrains.exposed.dao.id.UUIDTable

object Users : UUIDTable("users") {
    val account = varchar("account", 255).uniqueIndex()
    val password = varchar("password", 255)
    val nickname = varchar("nickname", 255)
}