package com.bangkit.crowdwisebali.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.crowdwisebali.data.remote.response.DetailPlacesResponse
import com.bangkit.crowdwisebali.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import com.bangkit.crowdwisebali.data.local.FavoriteEntity
import com.bangkit.crowdwisebali.data.local.FavoriteRepository
import com.bangkit.crowdwisebali.data.remote.response.DataDetail
import java.util.Locale

class DetailViewModel(private val repository: FavoriteRepository) : ViewModel() {
    private val _placesDetail = MutableLiveData<DataDetail?>()
    val placesDetail: LiveData<DataDetail?> = _placesDetail

    private var _isDetailLoading = MutableLiveData<Boolean>()
    val isDetailLoading: LiveData<Boolean> = _isDetailLoading

    private val _snackBarText = MutableLiveData<String>()
    val snackBarText: LiveData<String> = _snackBarText

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    fun fetchPlacesDetail(placesId: String) {
        _isDetailLoading.value = true
        viewModelScope.launch {
            try {
                val apiService = ApiConfig.getApiService()
                val language = Locale.getDefault().language
                val response: DetailPlacesResponse = apiService.getDetailPlace(placesId, language)

                _isDetailLoading.value = false

                if (response.data != null) {
                    _placesDetail.value = response.data
                } else {
                    _snackBarText.value = "Error: No data available"
                }
            } catch (e: Exception) {
                _isDetailLoading.value = false
                _snackBarText.value = "Failure: ${e.message}"
            }
        }
    }

    fun checkFavoriteStatus(placeId: String) {
        repository.getFavoriteById(placeId).observeForever { favorite ->
            _isFavorite.value = favorite != null
        }
    }

    fun addToFavorite(place: DataDetail) {

        val photosList = place.photos?.map { it?.photoReference ?: "" } ?: emptyList()
        val rating = (place.rating as? Number)?.toFloat() ?: 0f
        val favoriteEntity = FavoriteEntity(
            id = place.placeId ?: "",
            name = place.placeName,
            location = place.googleMapsLink,
            rating = rating,
            userRatingCount = place.userRatingCount,
            photosItem = photosList
        )
        repository.insert(favoriteEntity)

        viewModelScope.launch {
            repository.insert(favoriteEntity)
            _isFavorite.value = true
        }
    }

    fun deleteFromFavorite(placeId: String) {
        val favoriteEntity = FavoriteEntity(id = placeId)

        viewModelScope.launch {
            repository.delete(favoriteEntity)
            _isFavorite.value = false
        }
    }
}
