package com.bangkit.crowdwisebali.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.crowdwisebali.data.pref.SettingPreferences

class ProfileFactory(private val pref: SettingPreferences): ViewModelProvider.NewInstanceFactory() {
    @Suppress ("UNCHECKED_CAST")
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ProfileViewModel::class.java)){
            return ProfileViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknow ViewModel class: " + modelClass.name)
    }
}