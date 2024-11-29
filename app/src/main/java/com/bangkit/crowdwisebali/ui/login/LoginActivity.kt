package com.bangkit.crowdwisebali.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.crowdwisebali.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.sign.setOnClickListener {
//            signIn()
//        }

    }

//    private fun signIn() {
//        val credentialManager = CredentialManager.create(this) //import from androidx.CredentialManager
//        val googleIdOption = GetGoogleIdOption.Builder()
//            .setFilterByAuthorizedAccounts(false)
//            .setServerClientId(getString(R.string.your_web_client_id))
//            .build()
//        val request = GetCredentialRequest.Builder() //import from androidx.CredentialManager
//            .addCredentialOption(googleIdOption)
//            .build()
//    }
}