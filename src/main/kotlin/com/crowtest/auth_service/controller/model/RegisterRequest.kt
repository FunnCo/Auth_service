package com.crowtest.auth_service.controller.model

data class RegisterRequest (
        val firstname: String?,
        val lastname: String?,
        val patronymic: String?,
        val email: String?,
        val phone: String?,
        val password: String?,
        val group: String?,
)
