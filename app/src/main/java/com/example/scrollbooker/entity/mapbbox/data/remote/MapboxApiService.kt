package com.example.scrollbooker.entity.mapbbox.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MapboxApiService {
    @GET("geocoding/v5/mapbox.places/{query}.json")
    suspend fun searchPlaces(
        @Path("query") query: String,
        @Query("access_token") accessToken: String,
        @Query("autocomplete") autocomplete: Boolean = true,
        @Query("limit") limit: Int = 5,
        @Query("country") country: String = "RO"
    ): AddressResponseDto
}