package com.crowtest.auth_service.controller

import com.crowtest.auth_service.dto.DtoMapper
import com.crowtest.auth_service.dto.request.RegisterRequest
import com.crowtest.auth_service.dto.response.ResponseUserDto
import com.crowtest.auth_service.repository.UserRepository
import com.crowtest.auth_service.service.JwtService
import com.crowtest.auth_service.service.UserService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*
import kotlin.jvm.optionals.getOrNull

@RestController
@RequestMapping("/api/v2/user")
class UserController {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var dtoMapper: DtoMapper

    @Autowired
    lateinit var jwtService: JwtService

    @PostMapping("/change_info")
    fun changeInfo(@RequestBody request: RegisterRequest, @RequestParam userId:String):ResponseEntity<Void> {
        try {
            userService.update(request, userId)
        } catch (e: ResponseStatusException){
            return ResponseEntity.badRequest().build()
        }
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @OptIn(ExperimentalStdlibApi::class)
    @GetMapping("/get/{userId}")
    fun getUserInfo(
        @PathVariable userId: String
    ): ResponseEntity<ResponseUserDto>{
        val user = userRepository.findById(UUID.fromString(userId)).getOrNull()
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User with such id is not found")
        return ResponseEntity.ok(dtoMapper.mapUserEntityToDto(user))
    }


    @GetMapping("/get")
    fun getUserInfo(
        httpServletRequest: HttpServletRequest
    ): ResponseEntity<ResponseUserDto>{
        val userId = jwtService.extractUsername(httpServletRequest.getHeader("Authorization").substring(7))
        val user = userRepository.findById(UUID.fromString(userId)).get()
        return ResponseEntity.ok(dtoMapper.mapUserEntityToDto(user))
    }

}