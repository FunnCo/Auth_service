package com.crowtest.auth_service.config

import com.crowtest.auth_service.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@Configuration
class ApplicationConfig {

    @Autowired
    lateinit var userRepository: UserRepository

    @OptIn(ExperimentalStdlibApi::class)
    @Bean
    fun userDetailsService():UserDetailsService{
        return UserDetailsService { username -> userRepository.findByEmail(username) ?: userRepository.findById(UUID.fromString(username)).getOrNull() ?: throw UsernameNotFoundException("User not found") }
    }

    @Bean
    fun authenticationProvider():AuthenticationProvider{
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(userDetailsService())
        authProvider.setPasswordEncoder(passwordEncoder())
        return authProvider
    }

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager = config.authenticationManager

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()
}