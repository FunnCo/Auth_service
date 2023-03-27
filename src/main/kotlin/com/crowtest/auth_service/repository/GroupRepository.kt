package com.crowtest.auth_service.repository

import com.crowtest.auth_service.entity.Group
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface GroupRepository : CrudRepository<Group, UUID> {
    fun findByName(name: String): Group?
    fun existsByName(name: String): Boolean
}