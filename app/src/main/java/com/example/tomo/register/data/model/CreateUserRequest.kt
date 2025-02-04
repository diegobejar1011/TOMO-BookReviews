package com.example.tomo.register.data.model

data class CreateUserRequest (
    val username: String,
    val email: String,
    val password: String
)