package com.app.bikercopilot.auth.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amplifyframework.auth.AuthCategory
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.cognito.options.AWSCognitoAuthSignUpOptions
import com.amplifyframework.auth.result.step.AuthSignUpStep
import com.amplifyframework.core.Amplify
import com.app.bikercopilot.BaseViewModel
import com.app.bikercopilot.auth.AuthNavigator
import com.app.bikercopilot.common.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider
) : BaseViewModel(), SignUpDataValidator.ValidationResult {

    @Inject
    lateinit var signUpDataValidator: SignUpDataValidator
    private lateinit var authNavigator: AuthNavigator
    private var auth: AuthCategory = Amplify.Auth

    //Livedata
    private val signUpViewState = MutableLiveData<SignUpViewState>()
    internal fun getSignUpState(): LiveData<SignUpViewState> = signUpViewState

    fun setNavigator(authNavigator: AuthNavigator) {
        if (this::authNavigator.isInitialized.not()) {
            this.authNavigator = authNavigator
        }
    }

    fun navigateBack() {
        authNavigator.navigateUp()
    }

    fun createUser(bikerSignUpUiModel: BikerSignUpUiModel) {
        signUpDataValidator.validate(this, bikerSignUpUiModel)
    }

    override fun onSignUpValidationPasses(bikerSignUp: BikerSignUpUiModel) {
        val authSignUpOptions =
            AWSCognitoAuthSignUpOptions.builder()
                .userAttribute(AuthUserAttributeKey.email(), bikerSignUp.email)
                .build()

        auth.signUp(bikerSignUp.email, bikerSignUp.password, authSignUpOptions,
            {
                if (it.nextStep.signUpStep == AuthSignUpStep.CONFIRM_SIGN_UP_STEP) {
                    authNavigator.navigateSignUpToConfirm()
                }
            },
            {
                it.printStackTrace()
                signUpViewState.postValue(SignUpViewState.SignUpErrorViewState(it));
            })
    }

    override fun onSignUpValidationFailed(
        bikerSignUp: BikerSignUpUiModel,
        validationType: SignUpDataValidator.ValidationType
    ) {
        when (validationType) {
            SignUpDataValidator.ValidationType.EMPTY_USERNAME,
            SignUpDataValidator.ValidationType.EMPTY_EMAIL,
            SignUpDataValidator.ValidationType.EMPTY_PASSWORD,
            SignUpDataValidator.ValidationType.EMPTY_PASSWORD_CONFIRMATION -> {
                setSignUpValidationError(
                    validationType,
                    resourceProvider.getEmptyFieldErrorLabel()
                )
            }
            SignUpDataValidator.ValidationType.INVALID_EMAIL -> {
                setSignUpValidationError(
                    validationType,
                    resourceProvider.getInvalidEmailErrorLabel()
                )
            }
            SignUpDataValidator.ValidationType.INVALID_PASSWORD_LENGTH -> {
                setSignUpValidationError(
                    validationType,
                    resourceProvider.getInvalidPasswordLengthLabel()
                )
            }
            SignUpDataValidator.ValidationType.PASSWORD_CONFIRMATION_MATCH -> {
                setSignUpValidationError(
                    validationType,
                    resourceProvider.getPasswordConfirmationMissMatchErrorLabel()
                )
            }
            SignUpDataValidator.ValidationType.PASSWORD_CONTAINS_SPACES -> {
                setSignUpValidationError(
                    validationType,
                    resourceProvider.getPasswordContainingSpacesLabel()
                )
            }
            SignUpDataValidator.ValidationType.PASSWORD_MISSING_ALPHABET -> {
                setSignUpValidationError(
                    validationType,
                    resourceProvider.getPasswordMissingLettersLabel()
                )
            }
            SignUpDataValidator.ValidationType.PASSWORD_MISSING_DIGITS -> {
                setSignUpValidationError(
                    validationType,
                    resourceProvider.getPasswordMissingDigitsLabel()
                )
            }
            SignUpDataValidator.ValidationType.PASSWORD_MISSING_SPECIAL_CHARS -> {
                setSignUpValidationError(
                    validationType,
                    resourceProvider.getPasswordMissingSpecialCharsLabel()
                )
            }
        }

    }

    private fun setSignUpValidationError(
        signUpValidationFailedType: SignUpDataValidator.ValidationType,
        errorLabel: String
    ) {
        signUpViewState.postValue(
            SignUpViewState.SignupValidationFailedState(
                signUpValidationFailedType, errorLabel
            )
        )
    }

    fun verifySignUp(code: String) {
        auth.confirmSignUp("isc.rarroyo@gmail.com", code, {
            if (it.isSignUpComplete)
                signUpViewState.postValue(SignUpViewState.SignUpCompletedViewState)
            Log.wtf("confirmSignUp",  it.nextStep.toString())
        }, {
            Log.wtf("confirmSignUp", it.message)
            it.printStackTrace()
        });
    }

    fun resendCode(){
    }
}


