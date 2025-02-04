package com.example.tomo.login.data.repository

import com.example.tomo.core.network.RetrofitHelper
import com.example.tomo.login.data.model.UserValidateDTO
import com.example.tomo.login.data.model.UserValidateRequest

class LoginRepository {
    private val loginService = RetrofitHelper.loginService

    suspend fun validateUser(validateRequest: UserValidateRequest): Result<UserValidateDTO> {

        return try {
            val response = loginService.validateUser(validateRequest)

            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}