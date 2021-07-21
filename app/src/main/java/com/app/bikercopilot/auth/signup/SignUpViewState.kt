package com.app.bikercopilot.auth.signup

import com.amplifyframework.auth.AuthException

sealed class SignUpViewState {
    class SignupValidationFailedState(
        val validationType: SignUpDataValidator.ValidationType,
        val message: String
    ) : SignUpViewState()

    class SignUpErrorViewState(val authException: AuthException) : SignUpViewState()
    object SignUpCompletedViewState : SignUpViewState()
}

data class BikerSignUpUiModel(
    val name: String,
    var email: String,
    var password: String,
    var confirmedPassword: String
)