package com.example.iiittrial.domain

import com.example.iiittrial.util.Constants
import com.example.iiittrial.data.remote.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.Base_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}