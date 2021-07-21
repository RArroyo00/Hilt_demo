package com.app.bikercopilot.auth

import androidx.navigation.NavController
import com.app.bikercopilot.R
import com.app.bikercopilot.common.ext.navigateSafe
import java.lang.ref.WeakReference

class AuthNavigator(navController: NavController) {
    private val weakNavController = WeakReference(navController)

    fun getCurrentDestination() = weakNavController?.get()?.currentDestination

    private fun navigateSplashToProfile() {
        weakNavController.get()?.navigateSafe(
            R.id.action_splash_to_profile
        )
    }

    private fun navigateSplashToLogin() {
        weakNavController.get()?.navigateSafe(
            R.id.action_splash_to_login
        )
    }

    private fun navigateProfileToLogin() {
        weakNavController.get()?.navigateSafe(
            R.id.action_profileFragment_to_loginFragment
        )
    }

    private fun navigateLoginToProfile() {
        weakNavController.get()?.navigateSafe(
            R.id.action_login_to_profile
        )
    }

    fun navigateToProfile() {
        if (weakNavController.get()?.currentDestination?.id == R.id.loginFragment) {
            navigateLoginToProfile()
        } else {
            navigateSplashToProfile()
        }
    }

    fun navigateToLogin() {
        if (weakNavController.get()?.currentDestination?.id == R.id.profileFragment) {
            navigateProfileToLogin()
        } else {
            navigateSplashToLogin()
        }
    }

    fun navigateLoginToSignup() {
        weakNavController.get()?.navigateSafe(
            R.id.action_loginFragment_to_navigation_signup
        )
    }

    fun navigateSignUpToConfirm() {
        weakNavController.get()?.navigateSafe(
            R.id.action_signUpFragment_to_codeVerificationFragment
        )
    }

    fun navigateSplashToConfirm() {
        weakNavController.get()?.navigateSafe(
            R.id.action_splashFragment_to_navigation_signup
        )
    }

    fun navigateUp() {
        weakNavController.get()?.navigateUp()
    }
}