package com.deloitte.mostpopular.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.deloitte.mostpopular.ui.MainActivity
import com.deloitte.mostpopular.ui.authentication.AuthActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint()
class RoutingActivity : AppCompatActivity() {
    private val routingViewModel: RoutingViewModel by viewModels()

    lateinit var content: View
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = this.installSplashScreen()

        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { true }

        initObserveViewModel()

    }


    private fun initObserveViewModel() {
        lifecycleScope.launch {


            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {

                routingViewModel.uiEvent.collect {
                    when (it) {
                        RoutingViewModel.StartViewEvent.OpenAuthActivity -> startAuthActivity()
                        RoutingViewModel.StartViewEvent.OpenHomeActivity -> startMainActivity()
                        is RoutingViewModel.StartViewEvent.ShowToast -> {
                            showError(it.message)
                        }
                    }
                }


            }
        }
    }

    private fun startMainActivity() {
        startActivity(Intent(this@RoutingActivity, MainActivity::class.java))
        finish()
    }

    private fun startAuthActivity() {
        startActivity(Intent(this@RoutingActivity, AuthActivity::class.java))
        finish()
    }

    private fun showError(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }


}