package com.bangkit.crowdwisebali.ui.onboarding.screens

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.viewpager2.widget.ViewPager2
import com.bangkit.crowdwisebali.R
import com.bangkit.crowdwisebali.ui.login.LoginActivity

class FirstScreen : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_first_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val next = view.findViewById<Button>(R.id.next)
        val skip = view.findViewById<View>(R.id.skip)

        next.setOnClickListener {
            val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
            viewPager?.currentItem = 1
        }

        skip.setOnClickListener {
            val sharedPref: SharedPreferences = requireActivity().getSharedPreferences("onboarding_pref", MODE_PRIVATE)
            sharedPref.edit().putBoolean("onboarding_completed", true).apply()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }
}