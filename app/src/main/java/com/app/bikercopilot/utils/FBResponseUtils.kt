package com.app.bikercopilot.utils

import com.app.bikercopilot.domain.FBProfilePictureResponse

fun String.parseFBResponse(): FBProfilePictureResponse? {
    return deserialize(FBProfilePictureResponse::class.java, this)
}