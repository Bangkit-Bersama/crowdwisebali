package com.bangkit.crowdwisebali.data.remote.response

data class PredictionResponse(


    val data: DataPrediction,
    val status: String
)

data class DataPrediction(
    val occupancy: Double
)