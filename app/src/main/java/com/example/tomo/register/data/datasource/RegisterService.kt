package com.example.tomo.register.data.datasource

import com.example.tomo.register.data.model.CreateUserRequest
import com.example.tomo.register.data.model.UserDTO
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterService {

    @POST("user")
    suspend fun createUser(@Body request: CreateUserRequest): Response<UserDTO>

}