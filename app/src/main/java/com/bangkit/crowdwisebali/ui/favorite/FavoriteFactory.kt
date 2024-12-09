package com.bangkit.crowdwisebali.ui.favorite

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.crowdwisebali.data.local.FavoriteRepository
import com.bangkit.crowdwisebali.di.Injection

class FavoriteFactory private constructor(private val repository: FavoriteRepository) : ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: FavoriteFactory? = null
        fun getInstance(context: Context): FavoriteFactory =
            instance ?: synchronized(this) {
                instance ?: FavoriteFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}