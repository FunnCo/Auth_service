package com.crowtest.auth_service.dto.request

import java.io.Serializable

/**
 * A DTO for the {@link com.crowtest.auth_service.entity.Group} entity
 */
data class CreateGroupDto(val name: String? = null) : Serializable {

    fun isObjectValid(): Boolean{
        return name != null
    }


}