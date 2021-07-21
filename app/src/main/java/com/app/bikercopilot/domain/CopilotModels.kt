package com.app.bikercopilot.domain

import com.google.gson.annotations.SerializedName

data class Biker(
    val id: String,
    var name: String,
    var profilePicture: String,
    var profile: String
)

data class FBProfilePictureResponse (var data: Data? = null){
    data class Data (
        var height: Int? = null,
        @SerializedName("is_silhouette")
        var isSilhouette: Boolean? = null,
        var url: String? = null,
        var width: Int? = null
    )
}