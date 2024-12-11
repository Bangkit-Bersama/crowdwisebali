package com.bangkit.crowdwisebali.ui

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.crowdwisebali.MainActivity
import com.bangkit.crowdwisebali.R
import android.animation.ValueAnimator
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.bangkit.crowdwisebali.data.pref.SettingPreferences
import com.bangkit.crowdwisebali.data.pref.dataStore
import com.bangkit.crowdwisebali.ui.onboarding.OnBoardingActivity
import com.bangkit.crowdwisebali.ui.profile.ProfileFactory
import com.bangkit.crowdwisebali.ui.profile.ProfileViewModel

class SplashActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        imageView = findViewById(R.id.imageView)
        progressBar = findViewById(R.id.progressBar)

        val sharedPref = getSharedPreferences("onboarding_pref", MODE_PRIVATE)
        val isOnboardingCompleted = sharedPref.getBoolean("onboarding_completed", false)

        playAnimation()
        startLoadingAnimation()

        @Suppress("DEPRECATION")
        Handler().postDelayed({
            if (isOnboardingCompleted) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, OnBoardingActivity::class.java))
            }
            finish()
        }, 3000)

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

    private fun playAnimation() {
        ObjectAnimator.ofFloat(imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 2500
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    private fun startLoadingAnimation() {
        val animator = ValueAnimator.ofInt(0, 100)
        animator.duration = 3000
        animator.addUpdateListener {
            val progress = it.animatedValue as Int
            progressBar.progress = progress
        }
        animator.start()
    }
}
