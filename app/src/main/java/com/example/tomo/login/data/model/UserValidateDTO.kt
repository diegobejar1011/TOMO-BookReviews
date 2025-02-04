package com.example.tomo.login.data.model

data class UserValidateDTO (
    val message: String,
    val validate: Boolean,
    val token: String?,
    val id: Int?
)