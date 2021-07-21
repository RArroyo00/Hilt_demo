package com.app.bikercopilot.common

import androidx.annotation.ColorInt

interface ResourceProvider {
    fun getBikesDrawables():List<Int>
    fun getEmptyFieldErrorLabel(): String
    fun getInvalidEmailErrorLabel():String
    fun getInvalidPasswordLengthLabel():String
    fun getPasswordContainingSpacesLabel():String
    fun getPasswordMissingLettersLabel():String
    fun getPasswordMissingDigitsLabel():String
    fun getPasswordMissingSpecialCharsLabel():String
    fun getPasswordConfirmationMissMatchErrorLabel():String
    fun getNoAccountQuestionLabel():String
    fun getSignUpLabel():String
    fun getCodeVerificationDirections():String

    @ColorInt fun getMainColor(): Int
}