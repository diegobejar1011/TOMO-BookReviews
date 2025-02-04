package com.example.tomo.add.data.repository

import com.example.tomo.add.data.model.CreateReviewDTO
import com.example.tomo.add.data.model.CreateReviewRequest
import com.example.tomo.core.network.RetrofitHelper
import com.example.tomo.core.storage.TokenManager

class AddRepository (
    private val tokenManager: TokenManager
) {
    private val addService = RetrofitHelper.getAddService(tokenManager)

    suspend fun createReview(createReviewRequest: CreateReviewRequest): Result<CreateReviewDTO> {
        return try {

            val response = addService.createReview(tokenManager.getId(), createReviewRequest)

            if(response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string()))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }

    }
}