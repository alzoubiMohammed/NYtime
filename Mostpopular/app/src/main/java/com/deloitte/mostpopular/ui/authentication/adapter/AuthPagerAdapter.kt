package com.deloitte.mostpopular.ui.authentication.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.deloitte.mostpopular.ui.authentication.login.LoginFragment
import com.deloitte.mostpopular.ui.authentication.registration.RegistrationFragment

class AuthPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = FRAGMENT_COUNT // Two fragments

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> LoginFragment()
            1 -> RegistrationFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }

    companion object {
        const val FRAGMENT_COUNT = 2
    }
}
