package com.app.bikercopilot.bikerprofile

import com.app.bikercopilot.ui.models.BikerUIModel

sealed class ProfileViewState {
    object ProfileLoading : ProfileViewState()
    object ProfileError: ProfileViewState()
    class ProfileLoaded(val biker: BikerUIModel) : ProfileViewState()
}