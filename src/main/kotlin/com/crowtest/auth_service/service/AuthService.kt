package com.crowtest.auth_service.service

import com.crowtest.auth_service.dto.request.AuthRequest
import com.crowtest.auth_service.dto.response.AuthResponse
import com.crowtest.auth_service.dto.request.RegisterRequest
import com.crowtest.auth_service.entity.Group
import com.crowtest.auth_service.entity.User
import com.crowtest.auth_service.entity.enum.Role
import com.crowtest.auth_service.repository.GroupRepository
import com.crowtest.auth_service.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class AuthService {
    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var groupRepository: GroupRepository

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    lateinit var jwtService: JwtService

    @Autowired
    lateinit var authenticationManager: AuthenticationManager

    fun register(request: RegisterRequest, role: String?): AuthResponse? {

        if (request.email == null ||
            request.firstname == null ||
            request.lastname == null ||
            request.group == null ||
            request.phone == null ||
            request.password == null
        ) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "At least one of the required fields are empty")
        }


        var group = groupRepository.findByName(request.group!!)

        if (group == null) {
            group = Group(null, request.group)
            groupRepository.save(group)
        }

        val user = User(
            null,
            group,
            if (role != null && Role.values().any { enumRole -> enumRole.name == role }) Role.valueOf(role) else Role.STUDENT,
            request.firstname!!,
            request.lastname!!,
            request.patronymic,
            request.email!!,
            passwordEncoder.encode(request.password),
            request.phone!!
        )

        try {
            userRepository.save(user)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "User with such email or phone already exists")
        }
        val extraClaims = mutableMapOf<String, Any>("userGroupId" to user.group.groupId!!, "userRole" to user.role)
        val jwtToken = jwtService.generateToken(extraClaims, user)
        return AuthResponse(jwtToken)
    }

    private val logger = LoggerFactory.getLogger(this::class.java.simpleName)

    fun authenticate(request: AuthRequest): AuthResponse? {
        authenticationManager.authenticate(UsernamePasswordAuthenticationToken(request.email, request.password))
        val user = userRepository.findByEmail(request.email)!!
        val extraClaims = mutableMapOf<String, Any>("userGroupId" to user.group.groupId!!, "userRole" to user.role)
        val jwtToken = jwtService.generateToken(extraClaims, user)
        return AuthResponse(jwtToken)
    }

}