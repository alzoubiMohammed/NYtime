package com.deloitte.mostpopular.ui.authentication.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.deloitte.mostpopular.databinding.FragmentLoginBinding
import com.deloitte.mostpopular.ui.authentication.login.LoginViewModel.LoginAction.*
import com.deloitte.mostpopular.ui.authentication.login.LoginViewModel.LoginEvent.OpenHomeScreen
import com.deloitte.mostpopular.ui.authentication.login.LoginViewModel.LoginEvent.ShowToast
import com.deloitte.mostpopular.ui.authentication.login.LoginViewModel.LoginState.Idle
import com.deloitte.mostpopular.ui.authentication.login.LoginViewModel.LoginState.Loading
import com.deloitte.mostpopular.ui.authentication.login.LoginViewModel.LoginState.OnValidationChange
import com.deloitte.mostpopular.ui.util.extension.hideKeyboard
import com.deloitte.mostpopular.ui.util.extension.setOnSingleClickListener
import com.deloitte.mostpopular.ui.util.extension.showToast
import com.deloitte.mostpopular.ui.util.extension.visibility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        initObserveViewModel()

    }

    private fun initListener() {
        binding.apply {
            editTextEmail.doAfterTextChanged { text ->
                viewModel.processUiAction(EmailChanged(text.toString()))
            }
            editTextPassword.doAfterTextChanged { text ->
                viewModel.processUiAction(PasswordChanged(text.toString()))
            }
            buttonLogin.setOnSingleClickListener {
                it.hideKeyboard()
                viewModel.processUiAction(Login)

            }
        }

    }

    private fun initObserveViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect {
                        showProgress(it is Loading)
                        when (it) {
                            Idle -> resetUi()
                            is OnValidationChange -> handelValidationError(it.state)

                            else -> {}
                        }
                    }
                }

                launch {
                    viewModel.uiEvent.collect {

                        when (it) {
                            OpenHomeScreen -> goToMainActivity()
                            is ShowToast -> showToast(it.message)
                        }
                    }
                }


            }


        }

    }

    private fun resetUi() {
        binding.apply {
            textInputLayoutEmail.error = null
            textInputLayoutPassword.error = null
        }
    }


    private fun showProgress(show: Boolean) {
        binding.progress.root.visibility(show)
    }

    private fun handelValidationError(state: LoginValidationFormState) {
        binding.apply {
            state.emailError?.let {
                textInputLayoutEmail.error = getString(it)
            }
            state.passwordError?.let {
                textInputLayoutPassword.error = getString(it)
            }

        }
    }

    private fun goToMainActivity() {
     TODO()
    }
}