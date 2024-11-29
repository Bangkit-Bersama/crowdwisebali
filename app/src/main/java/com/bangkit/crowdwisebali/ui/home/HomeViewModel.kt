package com.bangkit.crowdwisebali.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.crowdwisebali.data.remote.response.RecommendationResponse
import com.bangkit.crowdwisebali.data.remote.response.SearchResultItem
import com.bangkit.crowdwisebali.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _recommendation = MutableLiveData<List<SearchResultItem>>()
    val recommendation: LiveData<List<SearchResultItem>> = _recommendation

    private val _isRecommendationLoading = MutableLiveData<Boolean>()
    val isRecommendationLoading: LiveData<Boolean> = _isRecommendationLoading

    private val _snackbarText = MutableLiveData<String>()
    val snackbarText: LiveData<String> = _snackbarText

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
                    // Ambil searchResult dari data dan tetapkan ke _recommendation
                    _recommendation.value = response.body()?.data?.searchResult
                    if (_recommendation.value == null) {
                        Log.e("API_RESPONSE", "searchResult is null")
                    }
                } else {
                    _snackbarText.value = "Gagal memuat data: ${response.message()}"
                    Log.e("API_ERROR", "API error: ${response.errorBody()?.string()}")
                }
            }


            override fun onFailure(call: Call<RecommendationResponse>, t: Throwable) {
                _isRecommendationLoading.value = false
                _snackbarText.value = "Koneksi gagal: ${t.message}"
            }
        })
    }
}