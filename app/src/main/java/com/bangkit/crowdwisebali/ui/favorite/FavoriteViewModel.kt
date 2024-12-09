package com.bangkit.crowdwisebali.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.crowdwisebali.data.local.FavoriteEntity
import com.bangkit.crowdwisebali.data.local.FavoriteRepository

class FavoriteViewModel(private val placeRepository: FavoriteRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() =_isLoading

    private val _favorite = MutableLiveData<List<FavoriteEntity>>()
    val favorite: LiveData<List<FavoriteEntity>> get() = _favorite

    init {
        loadFavorite()
    }

    private fun loadFavorite(){
        _isLoading.value = true
        placeRepository.getAllFavorite().observeForever { places ->
            _isLoading.value = false
            _favorite.value = places
        }
    }
}