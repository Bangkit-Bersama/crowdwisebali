package com.bangkit.crowdwisebali.ui.onboarding

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.bangkit.crowdwisebali.MainActivity
import com.bangkit.crowdwisebali.R
import com.google.android.material.tabs.TabLayoutMediator

class OnBoardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        viewPager = findViewById(R.id.viewPager)
        val adapter = OnBoardingAdapter(this)
        viewPager.adapter = adapter

//        val tabLayout = findViewById<com.google.android.material.tabs.TabLayout>(R.id.tabLayout)
//        TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()

        // Simpan status onboarding selesai jika mencapai halaman terakhir
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == adapter.itemCount - 1) {
                    val sharedPref: SharedPreferences =
                        getSharedPreferences("onboarding_pref", MODE_PRIVATE)
                    sharedPref.edit().putBoolean("onboarding_completed", true).apply()
                }
            }
        })
    }
}