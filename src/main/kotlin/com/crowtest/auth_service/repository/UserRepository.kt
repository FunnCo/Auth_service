package com.crowtest.auth_service.repository

import com.crowtest.auth_service.entity.User
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface UserRepository : CrudRepository<User, UUID> {
    fun findByEmail(email: String): User?
}