package com.app.bikercopilot.common

import android.content.Context
import com.app.bikercopilot.R
import javax.inject.Inject

class ResourceProviderImpl @Inject constructor(val context: Context) : ResourceProvider {
    override fun getBikesDrawables(): List<Int> {
       return listOf(
           R.drawable.ic_bike,
           R.drawable.ic_bike_cafe,
           R.drawable.ic_bike_racing,
           R.drawable.ic_bike_scooter,
           R.drawable.ic_bike_sport)
    }

    override fun getEmptyFieldErrorLabel(): String {
       return context.getString(R.string.required_field)
    }

    override fun getInvalidEmailErrorLabel(): String {
       return context.getString(R.string.invalid_email)
    }

    override fun getInvalidPasswordLengthLabel(): String {
        return context.getString(R.string.invalid_password_length)
    }

    override fun getPasswordContainingSpacesLabel(): String {
        return context.getString(R.string.password_contains_spaces)
    }

    override fun getPasswordMissingLettersLabel(): String {
        return context.getString(R.string.missing_letters)
    }

    override fun getPasswordMissingDigitsLabel(): String {
        return context.getString(R.string.missing_numbers)
    }

    override fun getPasswordMissingSpecialCharsLabel(): String {
        return context.getString(R.string.missing_special_chars)
    }

    override fun getPasswordConfirmationMissMatchErrorLabel(): String {
        return context.getString(R.string.passwords_dont_match)
    }

    override fun getNoAccountQuestionLabel(): String {
        return context.getString(R.string.no_account)
    }

    override fun getSignUpLabel(): String {
        return context.getString(R.string.sign_up)
    }

    override fun getCodeVerificationDirections(): String {
       return context.getString(R.string.code_verification_directions)
    }

    override fun getMainColor(): Int {
       return context.getColor(R.color.purple_700)
    }

}