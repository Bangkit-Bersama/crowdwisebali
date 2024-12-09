package com.bangkit.crowdwisebali.di

import android.content.Context
import com.bangkit.crowdwisebali.data.local.FavoriteRepository

object Injection {
    fun provideRepository(application: Context): FavoriteRepository {
        return FavoriteRepository.getInstance(application)
    }
}