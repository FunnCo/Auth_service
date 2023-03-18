package com.crowtest.auth_service.controller

import com.crowtest.auth_service.controller.model.RegisterRequest
import com.crowtest.auth_service.repository.UserRepository
import com.crowtest.auth_service.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/v2/user")
class UserController {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var userService: UserService

    @PostMapping("/change_info")
    fun changeInfo(@RequestBody request: RegisterRequest, @RequestParam oldEmail:String):ResponseEntity<String> {
        try {
            userService.update(request, oldEmail)
        } catch (e: ResponseStatusException){
            return ResponseEntity.badRequest().build()
        }
        return ResponseEntity.ok("Success!!")
    }

}