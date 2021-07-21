package com.app.bikercopilot.auth

sealed class UserAuthViewState {
    object IdleAuthState: UserAuthViewState()
    object ValidatingAuthState: UserAuthViewState()
    object UnauthenticatedViewState: UserAuthViewState()
    object AuthenticatedViewState: UserAuthViewState()
    object AuthenticationErrorViewState: UserAuthViewState()
}