package com.app.bikercopilot.auth.signup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.app.bikercopilot.BaseFragment
import com.app.bikercopilot.BuildConfig
import com.app.bikercopilot.R
import com.app.bikercopilot.common.ext.*
import com.app.bikercopilot.databinding.FragmentSignUpBinding

class SignUpFragment : BaseFragment() {
    private lateinit var signUpViewModel: SignUpViewModel
    private lateinit var binding: FragmentSignUpBinding

    override fun onFragmentCreated() {
        super.onFragmentCreated()
        signUpViewModel = ViewModelProvider(requireActivity()).get(SignUpViewModel::class.java)
        observe(signUpViewModel.getSignUpState(), ::onSignUpState)
        lifecycle.addObserver(signUpViewModel)
    }

    private fun onSignUpState(signUpViewState: SignUpViewState?) {
        when (signUpViewState) {
            is SignUpViewState.SignUpErrorViewState -> {
            handleErrorState(signUpViewState)
            }
            is SignUpViewState.SignupValidationFailedState -> {
                drawDataError(signUpViewState.validationType, signUpViewState.message)
            }
        }
    }

    private fun handleErrorState(state: SignUpViewState.SignUpErrorViewState) {
        Log.wtf("handleErrorState", state.authException.message)
        state.authException.printStackTrace()
    }

    private fun drawDataError(validationType: SignUpDataValidator.ValidationType, message: String) {
        Log.wtf("drawDataError", validationType.name)
        when (validationType) {
            SignUpDataValidator.ValidationType.EMPTY_USERNAME -> {
                binding.txtName.putError(message)
            }
            SignUpDataValidator.ValidationType.EMPTY_EMAIL,
            SignUpDataValidator.ValidationType.INVALID_EMAIL -> {
                binding.txtEmail.putError(message)
            }
            SignUpDataValidator.ValidationType.INVALID_PASSWORD_LENGTH,
            SignUpDataValidator.ValidationType.EMPTY_PASSWORD,
            SignUpDataValidator.ValidationType.PASSWORD_CONTAINS_SPACES,
            SignUpDataValidator.ValidationType.PASSWORD_MISSING_ALPHABET,
            SignUpDataValidator.ValidationType.PASSWORD_MISSING_DIGITS,
            SignUpDataValidator.ValidationType.PASSWORD_MISSING_SPECIAL_CHARS -> {
                binding.txtPassword.putError(message)
            }
            SignUpDataValidator.ValidationType.EMPTY_PASSWORD_CONFIRMATION -> {
                binding.txtPasswordConfirm.putError(message)
            }
            SignUpDataValidator.ValidationType.PASSWORD_CONFIRMATION_MATCH -> {
                binding.txtPassword.putError(message)
                binding.txtPasswordConfirm.putError(message)
            }
        }
    }


    private fun clearDataError() {
        binding.run {
            this.txtName.clearError()
            this.txtEmail.clearError()
            this.txtPassword.clearError()
            this.txtPasswordConfirm.clearError()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSignUpBinding.bind(view)
        this.binding = binding
        initViews(binding)
    }

    private fun initViews(binding: FragmentSignUpBinding) {
        with(binding) {
            backButton.buttonBack.setOnClickListener {
                signUpViewModel.navigateBack()
            }
            btnSignup.setOnClickListener {
                clearDataError()
                signUpViewModel.createUser(createSignUpData())
            }
            tieName.enableAutoClearError()
            tieMail.enableAutoClearError()
            tiePassword.enableAutoClearError()
            tiePasswordConfirm.enableAutoClearError()
            //TODO Remove pre populated data
            if (BuildConfig.DEBUG) {
                tieName.setText("Rulo");
                tieMail.setText("isc.rarroyo@gmail.com");
                tiePassword.setText("RuloPass00*");
                tiePasswordConfirm.setText("RuloPass00*");
            }
        }
    }

    private fun createSignUpData(): BikerSignUpUiModel {
        return binding.run {
            BikerSignUpUiModel(
                tieName.getValue(),
                tieMail.getValue(),
                tiePassword.getValue(),
                tiePasswordConfirm.getValue()
            )
        }
    }


}