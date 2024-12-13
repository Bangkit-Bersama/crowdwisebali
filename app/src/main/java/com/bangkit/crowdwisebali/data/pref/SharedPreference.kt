package com.bangkit.crowdwisebali.data.pref

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth

class SharedPreferenceManager(private val context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("your_pref_name", Context.MODE_PRIVATE)

    private fun saveToken(token: String) {
        sharedPreferences.edit().putString("token_key", token).apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString("token_key", null)
    }

    fun getFirebaseAuthToken(onTokenReceived: (String?) -> Unit) {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            currentUser.getIdToken(true).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result?.token
                    token?.let { saveToken(it) }
                    onTokenReceived(token)
                } else {
                    onTokenReceived(null)
                }
            }
        } else {
            onTokenReceived(null)
        }
    }

}