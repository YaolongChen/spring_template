package com.example.demo.core.security

import com.auth0.jwt.exceptions.TokenExpiredException
import com.example.demo.core.result.ResultCode
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(private val jwtUtil: JwtUtil) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        val token = authHeader.substring(7)

        try {
            val principal = jwtUtil.extractUserPrincipal(token)

            val authentication = UsernamePasswordAuthenticationToken(
                principal, null, emptyList()
            )
            authentication.details = WebAuthenticationDetailsSource().buildDetails(request)

            SecurityContextHolder.getContext().authentication = authentication
        } catch (e: TokenExpiredException) {
            request.setAttribute("jwt_error", ResultCode.TOKEN_EXPIRED)
        } catch (e: Exception) {
            request.setAttribute("jwt_error", ResultCode.TOKEN_VERIFICATION_ERROR)
        }

        filterChain.doFilter(request, response)
    }

}