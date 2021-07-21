package com.app.bikercopilot.auth.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.app.bikercopilot.BaseFragment
import com.app.bikercopilot.R
import com.app.bikercopilot.databinding.FragmentCodeVerificationBinding
import com.poovam.pinedittextfield.PinField

class CodeVerificationFragment : BaseFragment() {
    private lateinit var signUpViewModel: SignUpViewModel
    private lateinit var binding: FragmentCodeVerificationBinding

    override fun onFragmentCreated() {
        super.onFragmentCreated()
        signUpViewModel = ViewModelProvider(requireActivity()).get(SignUpViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_code_verification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentCodeVerificationBinding.bind(view)
        this.binding = binding
        initViews(binding)
    }

    private fun initViews(binding: FragmentCodeVerificationBinding) {
        binding.run {
            codeVerification.onTextCompleteListener = object : PinField.OnTextCompleteListener{
                override fun onTextComplete(enteredText: String): Boolean {
                    signUpViewModel.verifySignUp(enteredText)
                    return true
                }
            }
            btnSignUpConfirm.setOnClickListener {
                signUpViewModel.verifySignUp(codeVerification.text.toString())
            }
        }
    }
}