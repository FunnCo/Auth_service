package com.crowtest.auth_service.repository

import com.crowtest.auth_service.entity.User
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, Long> {
    fun findByEmail(email: String): User?
}