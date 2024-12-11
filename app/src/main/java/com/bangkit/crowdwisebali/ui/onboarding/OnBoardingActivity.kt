package com.bangkit.crowdwisebali.ui.onboarding

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bangkit.crowdwisebali.MainActivity
import com.bangkit.crowdwisebali.R
import com.bangkit.crowdwisebali.data.pref.SettingPreferences
import com.bangkit.crowdwisebali.data.pref.dataStore
import com.bangkit.crowdwisebali.ui.profile.ProfileFactory
import com.bangkit.crowdwisebali.ui.profile.ProfileViewModel
import com.google.android.material.tabs.TabLayoutMediator

class OnBoardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        viewPager = findViewById(R.id.viewPager)
        val adapter = OnBoardingAdapter(this)
        viewPager.adapter = adapter
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == adapter.itemCount - 1) {
                    val sharedPref: SharedPreferences =
                        getSharedPreferences("onboarding_pref", MODE_PRIVATE)
                    sharedPref.edit().putBoolean("onboarding_completed", true).apply()
                }
            }
        })

        val pref = SettingPreferences.getInstance(dataStore)
        profileViewModel = ViewModelProvider(this, ProfileFactory(pref))[ProfileViewModel::class.java]

        profileViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}