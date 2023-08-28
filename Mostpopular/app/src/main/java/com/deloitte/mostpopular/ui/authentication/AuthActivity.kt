package com.deloitte.mostpopular.ui.authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.deloitte.mostpopular.R
import com.deloitte.mostpopular.databinding.ActivityAuthBinding
import com.deloitte.mostpopular.ui.authentication.adapter.AuthPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    private var _binding: ActivityAuthBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupViewPager()
    }


    private fun setupViewPager() {
        binding.viewPager.adapter=AuthPagerAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.title_login)
                1 -> getString(R.string.title_registration)
                else -> null
            }
        }.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}