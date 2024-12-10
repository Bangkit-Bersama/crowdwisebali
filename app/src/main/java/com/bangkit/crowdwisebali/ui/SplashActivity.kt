package com.bangkit.crowdwisebali.ui

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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

        // Inisialisasi view
        imageView = findViewById(R.id.imageView)
        progressBar = findViewById(R.id.progressBar)

        // SharedPreferences untuk mengecek apakah onboarding sudah selesai
        val sharedPref = getSharedPreferences("onboarding_pref", MODE_PRIVATE)
        val isOnboardingCompleted = sharedPref.getBoolean("onboarding_completed", false)

        // Memulai animasi
        playAnimation()
        startLoadingAnimation()

        // Pindah ke MainActivity setelah delay
        Handler().postDelayed({
            if (isOnboardingCompleted) {
                // Jika sudah selesai, buka MainActivity
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                // Jika belum selesai, buka OnBoardingFragment
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
        animator.duration = 3000 // Sesuaikan durasi dengan Handler delay
        animator.addUpdateListener {
            val progress = it.animatedValue as Int
            progressBar.progress = progress
        }
        animator.start()
    }
}
