package com.app.bikercopilot.auth

import android.app.Activity
import android.util.Log
import com.amplifyframework.auth.AuthCategory
import com.amplifyframework.auth.AuthProvider
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.core.Amplify


class AuthenticationManager {

    companion object {
        val auth: AuthCategory = Amplify.Auth
    }

    fun init() {
        try {
            Amplify.addPlugin(AWSCognitoAuthPlugin())
        } catch (x: Amplify.AlreadyConfiguredException) {
            // Plugin already configured, not needed
        }
    }

    fun createUser(email: String, password: String) {
        val options = AuthSignUpOptions.builder()
            .userAttribute(AuthUserAttributeKey.email(), email)
            .build()
        auth.signUp(email, password, options,
            { Log.i("createUser", "Sign up succeeded: $it") },
            { Log.e("createUser", "Sign up failed", it) }
        )
    }

}