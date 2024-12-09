package com.bangkit.crowdwisebali.ui.profile

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.bangkit.crowdwisebali.R
import com.google.android.material.switchmaterial.SwitchMaterial
import android.provider.Settings
import android.widget.ImageView
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.lifecycle.lifecycleScope
import com.bangkit.crowdwisebali.data.pref.SettingPreferences
import com.bangkit.crowdwisebali.data.pref.dataStore
import com.bangkit.crowdwisebali.ui.login.LoginActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel

    private lateinit var auth: FirebaseAuth

    private lateinit var profilePicture: ImageView
    private lateinit var name: TextView
    private lateinit var email: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profilePicture = view.findViewById(R.id.profile_picture)
        name = view.findViewById(R.id.name)
        email = view.findViewById(R.id.tv_email)

        auth = FirebaseAuth.getInstance()

        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            name.text = it.displayName
            email.text = it.email
            it.photoUrl?.let { photoUrl ->
                Glide.with(this).load(photoUrl).into(profilePicture)
            }
        }

        val switchTheme = view.findViewById<SwitchMaterial>(R.id.switch_theme)

        // Use requireContext() to access application context
        val pref = SettingPreferences.getInstance(requireContext().dataStore)
        profileViewModel = ViewModelProvider(this, ProfileFactory(pref))[ProfileViewModel::class.java]

        profileViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            profileViewModel.saveThemeSettings(isChecked)
        }

        // Set OnClickListener for tv_about
        val tvAbout: TextView = view.findViewById(R.id.tv_about)
        tvAbout.setOnClickListener {
            val intent = Intent(requireContext(), AboutUsActivity::class.java)
            startActivity(intent)
        }

        // Set OnClickListener for tv_info_app
        val tvInfoApp: TextView = view.findViewById(R.id.tv_info_app)
        tvInfoApp.setOnClickListener {
            val intent = Intent(requireContext(), InfoActivity::class.java)
            startActivity(intent)
        }

        // Set OnClickListener for tv_language (for locale settings)
        val tvLanguage: TextView = view.findViewById(R.id.tv_language)
        tvLanguage.setOnClickListener {
            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(intent)
        }

        val tvLogout: TextView = view.findViewById(R.id.tv_logout)
        tvLogout.setOnClickListener{
            signOut()
        }
    }

    private fun signOut() {
        lifecycleScope.launch {
            try {
                // Firebase sign-out
                auth.signOut()

                // Clear credential state if API >= 34
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    val credentialManager = CredentialManager.create(requireContext())
                    val request = ClearCredentialStateRequest() // Add required data if needed
                    credentialManager.clearCredentialState(request)
                }

                // Redirect to LoginActivity
                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
