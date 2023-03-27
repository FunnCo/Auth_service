package com.crowtest.auth_service.dto.response

import java.io.Serializable
import java.util.*

/**
 * A DTO for the {@link com.crowtest.auth_service.entity.User} entity
 */
data class ResponseUserDto(
    val userId: String? = null,
    val groupName: String? = null,
    val firstname: String? = null,
    val lastname: String? = null,
    val patronymic: String? = null,
    val email: String? = null,
    val phone: String? = null
) : Serializable