package com.example.tomo.core.network

import com.example.tomo.add.data.datasource.AddService
import com.example.tomo.core.storage.TokenManager
import com.example.tomo.home.data.datasource.HomeService
import com.example.tomo.login.data.datasource.LoginService
import com.example.tomo.register.data.datasource.RegisterService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private const val BASE_URL = "http://13.216.15.115:3000/api/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun createAuthenticatedRetrofit(tokenManager: TokenManager): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val token = tokenManager.getToken()

                val newRequest = if (token != null) {
                    originalRequest.newBuilder()
                        .header("Authorization", token)
                        .build()
                } else {
                    originalRequest
                }

                chain.proceed(newRequest)
            }
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    val registerService: RegisterService by lazy {
        retrofit.create(RegisterService::class.java)
    }

    val loginService: LoginService by lazy {
        retrofit.create(LoginService::class.java)
    }

    fun getHomeService(tokenManager: TokenManager): HomeService =
        createAuthenticatedRetrofit(tokenManager).create(HomeService::class.java)

    fun getAddService(tokenManager: TokenManager): AddService =
        createAuthenticatedRetrofit(tokenManager).create(AddService::class.java)

}