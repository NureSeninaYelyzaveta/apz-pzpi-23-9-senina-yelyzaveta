package com.example.artprogressmobile.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "http://10.0.2.2:5100/"   // Базова адреса бекенду

    // Створення Retrofit сервісу
    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)                         // Встановлення адреси сервера
            .addConverterFactory(GsonConverterFactory.create())   // Конвертація JSON
            .build()
            .create(ApiService::class.java)
    }
}