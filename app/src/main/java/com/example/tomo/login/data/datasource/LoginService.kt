package com.example.tomo.login.data.datasource

import com.example.tomo.login.data.model.UserValidateDTO
import com.example.tomo.login.data.model.UserValidateRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {

    @POST("user/login")
    suspend fun validateUser(@Body request: UserValidateRequest): Response<UserValidateDTO>

}