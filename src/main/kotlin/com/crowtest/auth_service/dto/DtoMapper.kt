package com.crowtest.auth_service.dto

import com.crowtest.auth_service.dto.request.CreateGroupDto
import com.crowtest.auth_service.dto.response.ResponseGroupDto
import com.crowtest.auth_service.dto.response.ResponseUserDto
import com.crowtest.auth_service.entity.Group
import com.crowtest.auth_service.entity.User
import org.springframework.stereotype.Component

@Component
class DtoMapper {

    fun mapGroupEntityToDto(entity: Group): ResponseGroupDto{
        return ResponseGroupDto(
            entity.groupId!!.toString(), entity.name
        )
    }

    fun mapGroupDtoToEntity(dto: CreateGroupDto): Group{
        return Group(null, dto.name!!)
    }

    fun mapUserEntityToDto(user: User): ResponseUserDto{
        return ResponseUserDto(
            user.userId.toString(),
            user.group.name,
            user.firstname,
            user.lastname,
            user.patronymic,
            user.email,
            user.phone
        )
    }
}