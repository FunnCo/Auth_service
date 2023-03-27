package com.crowtest.auth_service.dto.response

import java.io.Serializable
import java.util.*

/**
 * A DTO for the {@link com.crowtest.auth_service.entity.Group} entity
 */
data class ResponseGroupDto(val group_id: String? = null, val name: String? = null) : Serializable