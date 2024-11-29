package com.bangkit.crowdwisebali.data.remote.retrofit

import com.bangkit.crowdwisebali.data.remote.response.RecommendationResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("recommendation")
    @Headers("Content-Type: application/json")
    fun getRecommendation(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("place_type") placeType: String
    ): Call<RecommendationResponse>

//    @GET("recommendation")
//    suspend fun getRecommendation(
//        @Body requestBody: Map<String, Any> // Body dalam format JSON
//    ): RecommendationResponse
//
//    @GET("recommendation")
//    suspend fun getRecommendation(
//        @Query("latitude") latitude: Double,
//        @Query("longitude") longitude: Double,
//        @Query("place_type") placeType: String
//    ): RecommendationResponse
}