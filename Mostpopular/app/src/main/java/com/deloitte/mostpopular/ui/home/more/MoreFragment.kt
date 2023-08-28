package com.deloitte.mostpopular.ui.home.more

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.deloitte.mostpopular.data.model.User
import com.deloitte.mostpopular.databinding.FragmentMoreBinding
import com.deloitte.mostpopular.ui.authentication.AuthActivity
import com.deloitte.mostpopular.ui.home.more.MoreViewModel.MoreViewAction.ChangeLanguage
import com.deloitte.mostpopular.ui.home.more.MoreViewModel.MoreViewAction.LogoutClicked
import com.deloitte.mostpopular.ui.home.more.MoreViewModel.MoreViewEvent.*
import com.deloitte.mostpopular.ui.home.more.MoreViewModel.MoreViewState.*
import com.deloitte.mostpopular.ui.splash.RoutingActivity
import com.deloitte.mostpopular.ui.util.extension.setOnSingleClickListener
import com.deloitte.mostpopular.ui.util.extension.showToast
import com.deloitte.mostpopular.ui.util.extension.visibility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoreFragment : Fragment() {

    private var _binding: FragmentMoreBinding? = null
    private val binding: FragmentMoreBinding get() = _binding!!

    private val viewModel: MoreViewModel by viewModels()


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoreBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        initObserveViewModel()
    }

    private fun initObserveViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiEvent.collect {
                        when (it) {
                            GoToAuthActivity -> openAuthActivity()
                            is ChangeLanguageAndResetApp -> changeLanguage(it.language)
                            is ShowToast -> showToast(it.message)
                        }
                    }
                }
                launch {
                    viewModel.uiState.collect {
                        showProgress(it is Loading)
                        when (it) {
                            is Success -> handleSuccess(it.user)
                            else -> {}
                        }
                    }
                }
            }
        }
    }


    private fun initListener() {
        binding.textViewLogout.setOnSingleClickListener {
            viewModel.processUiAction(LogoutClicked)
        }
        binding.textViewLanguage.setOnSingleClickListener {
           val tag= getLanguageTag()
            viewModel.processUiAction(ChangeLanguage(tag))

        }
    }

    private fun showProgress(show: Boolean) {
        binding.progress.root.visibility(show)
    }

    private fun handleSuccess(user: User) {

        binding.run {
            textViewName.text = user.fullName
            textViewEmail.text = user.email
            textViewNationalId.text = user.nationalId
            textViewPhone.text = user.phoneNumber
            textViewDate.text = user.dateOfBirth
        }

    }

    private fun openAuthActivity() {
        val intent = Intent(requireActivity(), AuthActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun changeLanguage(language: String) {
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(language)
        AppCompatDelegate.setApplicationLocales(appLocale)
        val intent = Intent(requireActivity(), RoutingActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

    }

    private fun getLanguageTag():String{
       return AppCompatDelegate.getApplicationLocales().toLanguageTags()
    }
}