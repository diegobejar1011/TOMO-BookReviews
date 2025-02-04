package com.example.tomo.register.data.repository

import com.example.tomo.core.network.RetrofitHelper
import com.example.tomo.register.data.model.CreateUserRequest
import com.example.tomo.register.data.model.UserDTO
import com.example.tomo.login.data.model.UserValidateDTO
import com.example.tomo.login.data.model.UserValidateRequest

class RegisterRepository {
    private val registerService = RetrofitHelper.registerService

    suspend fun createUser(request: CreateUserRequest): Result<UserDTO> {
        return try{
            val response = registerService.createUser(request)
            if(response.isSuccessful){
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}