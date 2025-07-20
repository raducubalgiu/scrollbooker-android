package com.example.scrollbooker.entity.search.data.remote
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApiService {
    @GET("search")
    suspend fun search(
        @Query("query") query: String,
        @Query("lng") lng: Float?,
        @Query("lat") lat: Float?
    ): List<SearchDto>
}