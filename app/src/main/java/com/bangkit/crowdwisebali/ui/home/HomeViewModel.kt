package com.bangkit.crowdwisebali.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.crowdwisebali.data.remote.response.RecommendationResponse
import com.bangkit.crowdwisebali.data.remote.response.SearchResultItem
import com.bangkit.crowdwisebali.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _recommendation = MutableLiveData<List<SearchResultItem>>()
    val recommendation: LiveData<List<SearchResultItem>> = _recommendation

    private val _isRecommendationLoading = MutableLiveData<Boolean>()
    val isRecommendationLoading: LiveData<Boolean> = _isRecommendationLoading

    private val _snackBarText = MutableLiveData<String>()
    val snackBarText: LiveData<String> = _snackBarText

    private fun sortRecommendationsByRatingAndUserCount(data: List<SearchResultItem>): List<SearchResultItem> {
        return data.sortedWith(
            compareByDescending<SearchResultItem> {
                when (val rating = it.rating) {
                    is Double -> rating
                    is Int -> rating.toDouble()
                    else -> 0.0
                }
            }.thenByDescending { it.userRatingCount ?: 0 }
        )
    }

    fun filterPlacesByType(data: List<SearchResultItem>): Map<String, List<SearchResultItem>> {
        val groupedData = mutableMapOf<String, MutableList<SearchResultItem>>()

        for (item in data){
            item.placeType?.forEach { type ->
                if (type != null && !groupedData.containsKey(type)) {
                    groupedData[type] = mutableListOf()
                }
                type?.let { groupedData[it]?.add(item) }
            }

        }
        Log.d("GroupedData", groupedData.toString())
        return groupedData
    }

    // Fetch recommendation data from API
    fun fetchRecommendation(latitude: Double, longitude: Double, placeType: String) {
        _isRecommendationLoading.value = true
        val client = ApiConfig.getApiService().getRecommendation(latitude, longitude, placeType)
        client.enqueue(object : Callback<RecommendationResponse> {
            override fun onResponse(
                call: Call<RecommendationResponse>,
                response: Response<RecommendationResponse>
            ) {
                _isRecommendationLoading.value = false
                if (response.isSuccessful) {
                    val rawData = response.body()?.data?.searchResult ?: emptyList()
                    val sortedData = sortRecommendationsByRatingAndUserCount(rawData)

                    _recommendation.value = sortedData

                    if (_recommendation.value.isNullOrEmpty()) {
                        Log.e("API_RESPONSE", "searchResult is null or empty")
                    }
                } else {
                    _snackBarText.value = "Gagal memuat data: ${response.message()}"
                    Log.e("API_ERROR", "API error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<RecommendationResponse>, t: Throwable) {
                _isRecommendationLoading.value = false
                _snackBarText.value = "Koneksi gagal: ${t.message}"
            }
        })
    }
}