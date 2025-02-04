package com.example.tomo.home.data.repository

import com.example.tomo.core.network.RetrofitHelper
import com.example.tomo.core.storage.TokenManager
import com.example.tomo.home.data.model.ReviewDTO

class HomeRepository(
    private val tokenManager: TokenManager
) {

    private val homeService = RetrofitHelper.getHomeService(tokenManager)

    suspend fun getReviews(): Result<ReviewDTO> {
        return try{
            val response = homeService.getReviews(tokenManager.getId())

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