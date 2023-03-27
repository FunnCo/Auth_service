package com.crowtest.auth_service.controller

import com.crowtest.auth_service.dto.DtoMapper
import com.crowtest.auth_service.dto.request.CreateGroupDto
import com.crowtest.auth_service.dto.response.ResponseGroupDto
import com.crowtest.auth_service.entity.Group
import com.crowtest.auth_service.repository.GroupRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@RestController
@RequestMapping("/api/v2/group")
class GroupController {

    private val logger = LoggerFactory.getLogger(this::class.java.simpleName)

    @Autowired
    private lateinit var groupRepository: GroupRepository

    @Autowired
    protected lateinit var dtoMapper: DtoMapper

    @PostMapping("/add")
    fun addGroup(
        @RequestBody newGroupDto: CreateGroupDto
    ): ResponseEntity<ResponseGroupDto> {
        if (!newGroupDto.isObjectValid()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "At least one of the required fields is null")
        }

        if (groupRepository.existsByName(newGroupDto.name!!)) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "Group with such name already exists")
        }
        var newGroup = dtoMapper.mapGroupDtoToEntity(newGroupDto)
        newGroup = groupRepository.save(newGroup)

        return ResponseEntity.status(HttpStatus.CREATED).body(dtoMapper.mapGroupEntityToDto(newGroup))
    }

    @GetMapping("/all")
    fun getAllGroups(): ResponseEntity<List<ResponseGroupDto>> {
        val result = mutableListOf<ResponseGroupDto>()
        for (group: Group in groupRepository.findAll()) {
            result.add(dtoMapper.mapGroupEntityToDto(group))
        }
        return ResponseEntity.ok(result)
    }

    @PostMapping("/validate")
    fun validateListOfGroups(
        @RequestBody flatGroups: List<String>
    ): Boolean {
        return flatGroups.all { item ->
            groupRepository.existsById(UUID.fromString(item))
        }
    }

//    @OptIn(ExperimentalStdlibApi::class)
//    @DeleteMapping("/{groupId}")
//    fun deleteGroup(
//        @PathVariable(name = "groupId") groupId: String?
//    ) {
//        if(groupId.isNullOrBlank()){
//            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad id")
//        }
//        val entity = groupRepository.findById(UUID.fromString(groupId)).getOrNull()
//            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "No group with such id found")
//
//        groupRepository.delete(entity)
//    }

    @OptIn(ExperimentalStdlibApi::class)
    @GetMapping("/{groupId}")
    fun getGroup(
        @PathVariable(name = "groupId") groupId: String?
    ): ResponseEntity<ResponseGroupDto> {
        if (groupId.isNullOrBlank()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad id")
        }
        val entity = groupRepository.findById(UUID.fromString(groupId)).getOrNull()
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "No group with such id found")

        return ResponseEntity.ok(dtoMapper.mapGroupEntityToDto(entity))
    }
}