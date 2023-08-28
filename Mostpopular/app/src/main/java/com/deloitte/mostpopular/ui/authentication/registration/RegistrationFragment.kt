package com.deloitte.mostpopular.ui.authentication.registration

import android.content.Intent
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
import com.deloitte.mostpopular.databinding.FragmentRegistrationBinding
import com.deloitte.mostpopular.ui.MainActivity
import com.deloitte.mostpopular.ui.authentication.registration.RegistrationViewModel.RegistrationAction.*
import com.deloitte.mostpopular.ui.authentication.registration.RegistrationViewModel.RegistrationEvent.*
import com.deloitte.mostpopular.ui.authentication.registration.RegistrationViewModel.RegistrationState.*
import com.deloitte.mostpopular.ui.util.extension.asBirthDataStringFormat
import com.deloitte.mostpopular.ui.util.extension.hideKeyboard
import com.deloitte.mostpopular.ui.util.extension.setOnSingleClickListener
import com.deloitte.mostpopular.ui.util.extension.showDatePicker
import com.deloitte.mostpopular.ui.util.extension.showToast
import com.deloitte.mostpopular.ui.util.extension.visibility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegistrationFragment : Fragment() {


    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!


    private val viewModel: RegistrationViewModel by viewModels()


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserveViewModel()
        initListener()
    }

    private fun initListener() {
        binding.apply {
            editTextEmail.doAfterTextChanged { text ->
                viewModel.processUiAction(EmailChanged(text.toString()))
            }
            editTextFullName.doAfterTextChanged { text ->
                viewModel.processUiAction(FullNameChanged(text.toString()))
            }
            editTextNationalId.doAfterTextChanged { text ->
                viewModel.processUiAction(NationalIdChanged(text.toString()))
            }
            editTextPhoneNumber.doAfterTextChanged { text ->
                viewModel.processUiAction(PhoneNumberChanged(text.toString()))
            }
            editTextPassword.doAfterTextChanged { text ->
                viewModel.processUiAction(PasswordChanged(text.toString()))
            }
            editTextDateOfBirth.doAfterTextChanged { text ->
                viewModel.processUiAction(DateOfBirthChanged(text.toString()))
            }
            buttonRegister.setOnSingleClickListener {
                it.hideKeyboard()
                viewModel.processUiAction(RegisterClicked)
            }
            editTextDateOfBirth.setOnSingleClickListener {
                showDatePicker{
                    editTextDateOfBirth.setText(it.asBirthDataStringFormat())
                }
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
            textInputLayoutFullName.error = null
            textInputLayoutDateOfBirth.error = null
            textInputLayoutNationalId.error = null
            textInputLayoutPhoneNumber.error = null
        }
    }

    private fun showProgress(show: Boolean) {
        binding.progress.root.visibility(show)
    }

    private fun handelValidationError(state: RegisterValidationFormState) {
        binding.apply {
            state.emailError?.let { textInputLayoutEmail.error = getString(it)}
            state.passwordError?.let { textInputLayoutPassword.error = getString(it)}
            state.fullNameError?.let { textInputLayoutFullName.error = getString(it)}
            state.dateOfBirthError?.let { textInputLayoutDateOfBirth.error = getString(it)}
            state.nationalIdError?.let { textInputLayoutNationalId.error = getString(it)}
            state.phoneNumberError?.let { textInputLayoutPhoneNumber.error = getString(it)}
        }


    }

    private fun goToMainActivity(){
        startActivity(Intent(requireContext(),MainActivity::class.java)).also {
            requireActivity().finish()
        }
    }

}