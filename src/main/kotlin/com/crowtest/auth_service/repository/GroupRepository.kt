package com.crowtest.auth_service.repository

import com.crowtest.auth_service.entity.Group
import org.springframework.data.repository.CrudRepository

interface GroupRepository : CrudRepository<Group, Long> {
    fun findByName(name: String): Group?
}