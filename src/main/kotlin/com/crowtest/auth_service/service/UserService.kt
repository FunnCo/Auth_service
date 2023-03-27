package com.crowtest.auth_service.service

import com.crowtest.auth_service.dto.request.RegisterRequest
import com.crowtest.auth_service.entity.Group
import com.crowtest.auth_service.repository.GroupRepository
import com.crowtest.auth_service.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service
class UserService {
    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var groupRepository: GroupRepository

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    fun update(request: RegisterRequest, userId: String): Boolean {

        val user = userRepository.findById(UUID.fromString(userId)).orElse(null)

        if (request.password != null) {
            user.userPassword = passwordEncoder.encode(request.password)
        }
        if (request.phone != null) {
            user.phone = request.phone
        }
        if (request.email != null) {
            user.email = request.email
        }
        if (request.firstname != null) {
            user.firstname = request.firstname
        }
        if (request.lastname != null) {
            user.lastname = request.lastname
        }
        if (request.patronymic != null) {
            user.patronymic = request.patronymic
        }
        if(request.group != null) {
            var group = groupRepository.findByName(request.group)

            if (group == null) {
                group = Group(null, request.group)
                groupRepository.save(group)
            }
            user.group = group
        }

        try {
            userRepository.save(user)
        } catch (e: Exception){
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "User with such email or phone already exists")
        }

        return true
    }
}