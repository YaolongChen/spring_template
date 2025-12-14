package com.example.demo.core.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    val jwtAuthenticationFilter: JwtAuthenticationFilter,
    val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf {
            it.disable()
        }.authorizeHttpRequests {
            it.requestMatchers("/auth/**").permitAll()
            it.anyRequest().authenticated()
        }.formLogin {
            it.disable()
        }.httpBasic {
            it.disable()
        }.sessionManagement {
            it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java).exceptionHandling {
            it.authenticationEntryPoint(jwtAuthenticationEntryPoint)
        }

        return http.build()
    }
}