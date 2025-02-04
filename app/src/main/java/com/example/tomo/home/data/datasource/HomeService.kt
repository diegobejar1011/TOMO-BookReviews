package com.example.tomo.home.data.datasource

import com.example.tomo.home.data.model.ReviewDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface HomeService {

    @GET("review/{id}")
    suspend fun getReviews(@Path("id") id: Int): Response<ReviewDTO>

}