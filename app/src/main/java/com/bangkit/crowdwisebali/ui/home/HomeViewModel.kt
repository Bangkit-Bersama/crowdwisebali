package com.bangkit.crowdwisebali.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    //Need Response recommendation from API
//    private val _recommendation = MutableLiveData<list<Nama Response(tunggu API)>>()
//    val recommendation: LiveData<List<Nama Response(tunggu API)>> = _recommendation

    private val _isRecommendationLoading = MutableLiveData<Boolean>()
    val isRecommendationLoading: LiveData<Boolean> = _isRecommendationLoading

    //call the data from API
//    fun fetchRecommendation(){
//        val client = ApiConfig.getApiService().namaApi(token?) //ganti namaApi(token?) kalo dah ada ApiService
//        client.enqueue(object : Callback<NamaResponse> {
//            override fun onResponse(call: Call<NamaResponse>, response: Response<NamaResponse>) {
//                _isRecommendationLoading.value = false
//                if(response.isSuccessfull){
//                    _isRecommendationLoading.value = response.body()?.
//                }
//            }
//        })
//    }
}