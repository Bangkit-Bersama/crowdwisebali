package com.bangkit.crowdwisebali.ui.onboarding.screens

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.viewpager2.widget.ViewPager2
import com.bangkit.crowdwisebali.MainActivity
import com.bangkit.crowdwisebali.R
import com.bangkit.crowdwisebali.ui.login.LoginActivity

class SecondScreen : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_second_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val next = view.findViewById<Button>(R.id.next)
        val back = view.findViewById<Button>(R.id.back)
        val skip = view.findViewById<View>(R.id.skip)

        next.setOnClickListener {
            val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
            viewPager?.currentItem = 2
        }

        back.setOnClickListener {
            val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
            viewPager?.currentItem = 0
        }

        skip.setOnClickListener {
            // Intent untuk membuka MainActivity
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            // Menutup onboarding screen agar tidak bisa kembali ke layar ini
            activity?.finish()
        }
    }
}