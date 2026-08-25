package com.univap.lume.network

import retrofit2.http.GET
import retrofit2.http.Query

interface SpotifyBackendApi {

    @GET("api/spotify/search")
    suspend fun searchSpotify(
        @Query("q") query: String
    ): SpotifySearchResponse
}