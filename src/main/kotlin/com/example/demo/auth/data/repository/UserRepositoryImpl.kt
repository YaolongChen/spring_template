package com.example.demo.auth.data.repository

import com.example.demo.auth.data.persistence.entity.Users
import com.example.demo.auth.data.persistence.entity.Users.id
import com.example.demo.auth.domain.model.User
import com.example.demo.auth.domain.repository.UserRepository
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
@Transactional
class UserRepositoryImpl : UserRepository {
    override fun findUserByAccount(account: String): User? {
        val row = Users.selectAll().where { Users.account.eq(account) }.singleOrNull()

        return row?.let {
            User(
                id = it[id].value,
                account = it[Users.account],
                password = it[Users.password],
                nickname = it[Users.nickname],
            )
        }
    }

    override fun createUser(user: User): User {
        val id = Users.insert {
            it[account] = user.account
            it[password] = user.password
            it[nickname] = user.nickname
        } get id
        return user.copy(id = id.value)
    }

    override fun findUserById(id: UUID): User? {
        val row = Users.selectAll().where { Users.id.eq(id) }.singleOrNull()

        return row?.let {
            User(
                id = it[Users.id].value,
                account = it[Users.account],
                password = it[Users.password],
                nickname = it[Users.nickname],
            )
        }
    }
}