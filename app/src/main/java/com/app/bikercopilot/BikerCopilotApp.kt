package com.app.bikercopilot

import android.app.Application
import android.util.Log
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.AWSDataStorePlugin
import com.app.bikercopilot.auth.AuthenticationManager
import com.app.bikercopilot.bikerprofile.BikerSharedPreferences
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


@HiltAndroidApp
open class BikerCopilotApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initAWS()
    }

    private fun initAWS() {
        try {
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.addPlugin(AWSDataStorePlugin())
            Amplify.configure(applicationContext)
            Log.i(this::class.java.simpleName, "Initialized Amplify")
        } catch (error: AmplifyException) {
            Log.e(this::class.java.simpleName, "Could not initialize Amplify", error)
        }
    }

}