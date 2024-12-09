package com.bangkit.crowdwisebali.ui.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bangkit.crowdwisebali.ui.onboarding.screens.FirstScreen
import com.bangkit.crowdwisebali.ui.onboarding.screens.SecondScreen
import com.bangkit.crowdwisebali.ui.onboarding.screens.ThirdScreen

class OnBoardingAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FirstScreen()
            1 -> SecondScreen()
            2 -> ThirdScreen()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}