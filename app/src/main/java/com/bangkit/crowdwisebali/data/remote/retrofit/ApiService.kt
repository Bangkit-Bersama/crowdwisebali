package com.bangkit.crowdwisebali.data.remote.retrofit

import com.bangkit.crowdwisebali.data.pref.PredictionRequest
import com.bangkit.crowdwisebali.data.remote.response.DetailPlacesResponse
import com.bangkit.crowdwisebali.data.remote.response.PredictionResponse
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

    @GET("places/{placeId}")
    suspend fun getDetailPlace(
        @Path("placeId") placeId: String,
        @Query("language") language: String
    ): DetailPlacesResponse

    @POST("prediction")
    @Headers("Content-Type:application/json")
    suspend fun sendPrediction(
        @Body predictionRequest: PredictionRequest
    ): PredictionResponse
}