package com.app.bikercopilot.auth.signup

import com.app.bikercopilot.common.Constants
import java.util.regex.Pattern
import javax.inject.Inject

class SignUpDataValidator @Inject constructor() {
    private val validationFailed = true
    private var validations = ArrayList<Validation>()

    init {
        addValidation(Validation { bikerData: BikerSignUpUiModel ->
            Pair(ValidationType.EMPTY_USERNAME, isValueBlankOrEmpty(bikerData.name))
        })
        addValidation(Validation { bikerData: BikerSignUpUiModel ->
            Pair(ValidationType.EMPTY_EMAIL, isValueBlankOrEmpty(bikerData.email))
        })
        addValidation(Validation { bikerData: BikerSignUpUiModel ->
            Pair(ValidationType.EMPTY_PASSWORD, isValueBlankOrEmpty(bikerData.password))
        })
        addValidation(Validation { bikerData: BikerSignUpUiModel ->
            Pair(
                ValidationType.EMPTY_PASSWORD_CONFIRMATION,
                isValueBlankOrEmpty(bikerData.confirmedPassword)
            )
        })
        addValidation(Validation { bikerData: BikerSignUpUiModel ->
            Pair(
                ValidationType.INVALID_EMAIL,
                isEmailInvalid(bikerData.email)
            )
        })
        addValidation(Validation { bikerData: BikerSignUpUiModel ->
            Pair(
                ValidationType.INVALID_PASSWORD_LENGTH, isPasswordLengthCorrect(bikerData.password)
            )
        })
        addValidation(Validation { bikerData: BikerSignUpUiModel ->
            Pair(
                ValidationType.PASSWORD_CONTAINS_SPACES,
                isPasswordContainingSpaces(bikerData.password)
            )
        })
        addValidation(Validation { bikerData: BikerSignUpUiModel ->
            Pair(
                ValidationType.PASSWORD_MISSING_ALPHABET,
                isPasswordContainingLetters(bikerData.password)
            )
        })
        addValidation(Validation { bikerData: BikerSignUpUiModel ->
            Pair(
                ValidationType.PASSWORD_MISSING_DIGITS,
                isPasswordContainingDigits(bikerData.password)
            )
        })
        addValidation(Validation { bikerData: BikerSignUpUiModel ->
            Pair(
                ValidationType.PASSWORD_MISSING_SPECIAL_CHARS,
                isPasswordContainingSpecialChars(bikerData.password)
            )
        })
        addValidation(Validation { bikerData: BikerSignUpUiModel ->
            Pair(
                ValidationType.PASSWORD_CONFIRMATION_MATCH,
                isPasswordConfirmationMatch(bikerData.password, bikerData.confirmedPassword)
            )
        })
    }

    fun validate(validationResult: ValidationResult, bikerSignUpData: BikerSignUpUiModel) {
        validations.sortedByDescending {
            it.sortOrder
        }.forEach {
            val validation = it.precondition(bikerSignUpData)
            if (validation.second == validationFailed) {
                validationResult.onSignUpValidationFailed(bikerSignUpData, validation.first)
                return
            }
        }
        validationResult.onSignUpValidationPasses(bikerSignUpData)
    }

    private fun addValidation(validation: Validation) {
        validations.add(validation)
    }

    //region validation methods
    private fun isValueBlankOrEmpty(value: String): Boolean {
        return value.isEmpty() || value.isBlank()
    }

    private fun isEmailInvalid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches().not()
    }

    private fun isPasswordLengthCorrect(password: String): Boolean {
        return password.length < Constants.PASSWORD_LENGTH
    }

    private fun isPasswordContainingSpaces(password: String): Boolean {
        return password.contains(" ")
    }

    private fun isPasswordContainingLetters(password: String): Boolean {
        return Pattern.compile("^(?=.*[a-z])(?=.*[A-Z]).+$") //letter upper or lower
            .matcher(password).matches().not()
    }

    private fun isPasswordContainingDigits(password: String): Boolean {
        return Pattern.compile("^(?=.*\\d).+$") //at least one digit
            .matcher(password).matches().not()
    }

    private fun isPasswordContainingSpecialChars(password: String): Boolean {
        return Pattern.compile("^(?=.*[-+_!@#\$%^&*?]).+$") //at least one special char
            .matcher(password).matches().not()
    }


    /*
    private fun isValidPassword(password: String): Boolean {
        return Pattern.compile("((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!*?^]).{8})")
            .matcher(password).matches().not()
    }
    */


    private fun isPasswordConfirmationMatch(
        password: String,
        passwordConfirmation: String
    ): Boolean {
        return password != passwordConfirmation
    }

    //endregion

    enum class ValidationType {
        EMPTY_USERNAME,
        EMPTY_EMAIL,
        EMPTY_PASSWORD,
        EMPTY_PASSWORD_CONFIRMATION,
        INVALID_EMAIL,
        INVALID_PASSWORD_LENGTH,
        PASSWORD_CONTAINS_SPACES,
        PASSWORD_MISSING_ALPHABET,
        PASSWORD_MISSING_DIGITS,
        PASSWORD_MISSING_SPECIAL_CHARS,
        PASSWORD_CONFIRMATION_MATCH
    }

    interface ValidationResult {
        fun onSignUpValidationPasses(bikerSignUp: BikerSignUpUiModel)
        fun onSignUpValidationFailed(
            bikerSignUp: BikerSignUpUiModel,
            validationType: ValidationType
        )
    }
}

data class Validation(
    val sortOrder: Int = 0,
    val precondition: (BikerSignUpUiModel) -> Pair<SignUpDataValidator.ValidationType, Boolean>
)