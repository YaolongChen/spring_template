package com.example.demo.core.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import org.springframework.stereotype.Component
import java.util.Date
import java.util.UUID

@Component
class JwtUtil {
    private val algorithm = Algorithm.HMAC256("secret")

    private val expirationTime = 1000 * 60 * 60 * 24

    fun generateToken(id: String, username: String): String {
        return JWT.create()
            .withSubject(username)
            .withClaim("id", id)
            .withIssuer("auth0")
            .withIssuedAt(Date())
            .withExpiresAt(Date(System.currentTimeMillis() + expirationTime))
            .sign(algorithm)
    }

    fun verifyToken(token: String): DecodedJWT {
        val verifier = JWT.require(algorithm).withIssuer("auth0").build()

        return verifier.verify(token)
    }

    fun extractUserPrincipal(token: String): UserPrincipal {
        val decodedJwt = verifyToken(token)
        val username = decodedJwt.subject
        val userId = decodedJwt.getClaim("id").asString()
        return UserPrincipal(username = username, id = UUID.fromString(userId))
    }
}