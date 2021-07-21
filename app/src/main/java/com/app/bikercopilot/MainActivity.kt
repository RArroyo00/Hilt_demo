package com.app.bikercopilot

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.app.bikercopilot.auth.AuthNavigator
import com.app.bikercopilot.auth.AuthViewModel
import com.app.bikercopilot.auth.AuthenticationManager
import com.app.bikercopilot.auth.UserAuthViewState
import com.app.bikercopilot.auth.signup.SignUpViewModel
import com.app.bikercopilot.auth.signup.SignUpViewState
import com.app.bikercopilot.common.ext.observe
import com.app.bikercopilot.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    private val signUpViewModel: SignUpViewModel by viewModels()

    @Inject
    lateinit var authenticationManager: AuthenticationManager

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observe(authViewModel.getUserAuthState(), ::onUserAuthState)
        observe(signUpViewModel.getSignUpState(),::onSignUpAuthState)
        lifecycle.addObserver(authViewModel)
        authViewModel.isUserAuthenticated()
    }

    private fun onSignUpAuthState(signUpViewState: SignUpViewState?) {
        when(signUpViewState){
            SignUpViewState.SignUpCompletedViewState -> {
                Log.wtf("onSignUpAuthState",signUpViewState.toString())
            }
            is SignUpViewState.SignUpErrorViewState -> {
                signUpViewState.authException.printStackTrace()
                Log.wtf("onSignUpAuthState",signUpViewState.authException.message)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        authViewModel.setNavigator(AuthNavigator(getNavController()))
        signUpViewModel.setNavigator(AuthNavigator(getNavController()))
    }

    override fun initializeViewModel() {
        Log.wtf("initializeViewModel", "called")
    }

    //TODO Verify if sign up is in progress
    private fun onUserAuthState(userAuthViewState: UserAuthViewState?) {
        when (userAuthViewState) {
            UserAuthViewState.AuthenticatedViewState -> {
                authViewModel.navigateToProfile()
            }
            UserAuthViewState.UnauthenticatedViewState -> {
                authViewModel.navigateToLogin()
            }
            else -> Unit
        }
    }

    private fun getNavController(): NavController {
        val navHostFragment = binding.mainActivityNavHost
        return navHostFragment.findNavController()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AWSCognitoAuthPlugin.WEB_UI_SIGN_IN_ACTIVITY_CODE) {
            authViewModel.handleWebUIResponse(data)
        }
    }
}