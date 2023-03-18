package com.crowtest.auth_service.controller

import com.crowtest.auth_service.controller.model.AuthRequest
import com.crowtest.auth_service.controller.model.AuthResponse
import com.crowtest.auth_service.controller.model.RegisterRequest
import com.crowtest.auth_service.repository.UserRepository
import com.crowtest.auth_service.service.AuthService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/v2/auth")
class AuthController {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var authService: AuthService

    @PostMapping("/register")
    // create new user in db
    fun registerNewUser(@RequestBody request: RegisterRequest):ResponseEntity<Any> {
        try {
            return ResponseEntity.ok(authService.register(request))
        } catch (e: ResponseStatusException){
            return ResponseEntity.badRequest().build()
        }
    }

    @PostMapping("/login")
    // create new user in db
    fun authenticate(@RequestBody request: AuthRequest):ResponseEntity<AuthResponse> {
        try {
            return ResponseEntity.ok(authService.authenticate(request))
        } catch (e: ResponseStatusException){
            return ResponseEntity.badRequest().build()
        }
    }
}