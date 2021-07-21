package com.app.bikercopilot.auth

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.app.bikercopilot.BaseFragment
import com.app.bikercopilot.R
import com.app.bikercopilot.common.Constants
import com.app.bikercopilot.common.ResourceProvider
import com.app.bikercopilot.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment() {
    private lateinit var authViewModel: AuthViewModel
    private lateinit var fragmentLoginBinding: FragmentLoginBinding

    @Inject
    lateinit var resourceProvider: ResourceProvider

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        FragmentLoginBinding.inflate(inflater)
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onFragmentCreated() {
        super.onFragmentCreated()
        authViewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentLoginBinding.bind(view)
        fragmentLoginBinding = binding
        initViews(binding)
    }

    private fun initViews(binding: FragmentLoginBinding) {
        binding.apply {
            btnSignInGoogle.setOnClickListener {
                authViewModel.loginGmail(requireActivity())
            }
            btnSignInFacebook.setOnClickListener {
                authViewModel.loginFacebook(requireActivity())
            }
            btnSignIn.setOnClickListener {
                authViewModel.login()
            }

            val spannableLeading = SpannableString(resourceProvider.getNoAccountQuestionLabel())
            val spannableTrailing = SpannableString(resourceProvider.getSignUpLabel())

            spannableTrailing.apply {
                setSpan(object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        authViewModel.signup()
                    }
                }, 0, spannableTrailing.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                setSpan(
                    ForegroundColorSpan(resourceProvider.getMainColor()),
                    0,
                    this.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            txtSignUp.apply {
                isClickable = true
                movementMethod = LinkMovementMethod.getInstance()
                highlightColor = this.context.getColor(android.R.color.transparent)
                text = TextUtils.concat(spannableLeading, Constants.EMPTY_SPACE, spannableTrailing)
            }
        }
    }
}