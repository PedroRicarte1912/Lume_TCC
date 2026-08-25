package com.univap.lume.activity

import SpotifyBackendApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://spotify-backend-goo1.onrender.com"
    val spotifyApi: SpotifyBackendApi by lazy {
         Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(SpotifyBackendApi::class.java)
        }
}