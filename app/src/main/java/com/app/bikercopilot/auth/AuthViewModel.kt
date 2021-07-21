package com.app.bikercopilot.auth

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.webkit.URLUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amplifyframework.auth.*
import com.amplifyframework.core.Amplify
import com.app.bikercopilot.BaseViewModel
import com.app.bikercopilot.R
import com.app.bikercopilot.bikerprofile.BikerSharedPreferences
import com.app.bikercopilot.common.Constants
import com.app.bikercopilot.domain.Biker
import com.app.bikercopilot.utils.parseFBResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val bikerSharedPreferences: BikerSharedPreferences,
) : BaseViewModel() {

    private lateinit var authNavigator: AuthNavigator
    private var auth: AuthCategory = Amplify.Auth

    //Livedata
    private val userAuthState = MutableLiveData<UserAuthViewState>()
    internal fun getUserAuthState(): LiveData<UserAuthViewState> = userAuthState

    init {
        userAuthState.value = UserAuthViewState.IdleAuthState
    }

    fun setNavigator(authNavigator: AuthNavigator) {
        if (this::authNavigator.isInitialized.not()) {
            this.authNavigator = authNavigator
        }
    }

    fun isUserAuthenticated() {
        userAuthState.value = UserAuthViewState.ValidatingAuthState
        auth.fetchAuthSession(
            {
                if (it.isSignedIn) {
                    userAuthState.postValue(UserAuthViewState.AuthenticatedViewState)
                } else {
                    userAuthState.postValue(UserAuthViewState.UnauthenticatedViewState)
                }
            },
            {
                userAuthState.postValue(UserAuthViewState.AuthenticationErrorViewState)
            }
        )
    }

    fun loginGmail(activity: Activity) {
        auth.signInWithSocialWebUI(AuthProvider.google(), activity, {
            Log.wtf("onUserAuthState", auth.currentUser.username)
            getUserData { userAuthState.postValue(UserAuthViewState.AuthenticatedViewState) }
        }, {
            Log.wtf("onUserAuthState", it.message)
            it.printStackTrace()
        })
    }

    fun loginFacebook(activity: Activity) {
        auth.signInWithSocialWebUI(AuthProvider.facebook(), activity, {
            Log.wtf("onUserAuthState", auth.currentUser.username)
            getUserData { userAuthState.postValue(UserAuthViewState.AuthenticatedViewState) }
        }, {
            Log.wtf("onUserAuthState", it.message)
            it.printStackTrace()
        })
    }

    fun login() {
        auth.signIn("isc.rarroyo@gmail.com", "RuloPass00*", {
            Log.wtf("login", it.isSignInComplete.toString())
            Log.wtf("login", it.nextStep.toString())
        }, {
            Log.wtf("login", it.message)
            it.printStackTrace()
        })
    }

    private fun getUserData(callback: () -> Unit) {
        auth.fetchUserAttributes({ userAttributes ->
            userAttributes.map {
                Log.wtf("getUserData", "${it.key} - ${it.value}")
            }
            bikerSharedPreferences.setBikerProfile(createBikerFromAttributes(userAttributes))
            /*
            val biker = Biker.builder()
                .userId(auth.currentUser.userId)
                .name(auth.currentUser.username)
                .picture("https://lh3.googleusercontent.com/a-/AOh14GhM0YvAq7ON576WZN5KN5QPuUv5WWdgjTdk7sY75qs=s96-c")
                .build()
            Amplify.DataStore.save(biker, {
                Log.wtf("onUserAuthState Saved", "${it.toString()}")
            }, {
                Log.wtf("onUserAuthState Error", "${it.message}")
            })
            */
            callback.invoke()
        }, {
            it.printStackTrace()
        })
    }

    private fun createBikerFromAttributes(attributes: List<AuthUserAttribute>): Biker {
        val attrMap = attributes.map { it.key to it.value }.toMap()
        var userProfilePicture = attrMap[AuthUserAttributeKey.picture()]?.run {
            if (URLUtil.isValidUrl(this)) {
                this
            } else {
                getProfilePictureFBResponse(this)
            }
        } ?: Constants.EMPTY_STRING

        return Biker(
            auth.currentUser.userId,
            attrMap.getOrDefault(AuthUserAttributeKey.name(), Constants.BIKER),
            userProfilePicture,
            Constants.EMPTY_STRING
        )
    }

    private fun getProfilePictureFBResponse(facebookResponse: String): String {
        return facebookResponse.parseFBResponse()?.let {
            it.data?.url
        } ?: Constants.EMPTY_STRING
    }

    fun handleWebUIResponse(data: Intent?) {
        if (authNavigator.getCurrentDestination()?.id == R.id.loginFragment) {
            auth.handleWebUISignInResponse(data)
        }
    }

    //section Navigation
    fun navigateBack() {
        authNavigator.navigateUp()
    }

    fun navigateToProfile() {
        authNavigator.navigateToProfile()
    }

    fun navigateToLogin() {
        authNavigator.navigateToLogin()
    }

    fun navigateToConfirm() {
        authNavigator.navigateSplashToConfirm()
    }

    fun signup() {
        authNavigator.navigateLoginToSignup()
    }

    fun signOut() {
        auth.signOut({
            bikerSharedPreferences.clearData()
            userAuthState.postValue(UserAuthViewState.UnauthenticatedViewState)
        }, {
            Log.wtf("SignOut", it.message)
            it.printStackTrace()
        })
    }


}
