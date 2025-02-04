package com.example.tomo.add.data.datasource

import com.example.tomo.add.data.model.CreateReviewDTO
import com.example.tomo.add.data.model.CreateReviewRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface AddService {

    @POST("review/{id}")
    suspend fun createReview(@Path("id") id:Int, @Body createReviewRequest: CreateReviewRequest): Response<CreateReviewDTO>
}