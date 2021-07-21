package com.app.bikercopilot.bikerprofile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.bikercopilot.BaseViewModel
import com.app.bikercopilot.ui.models.BikerUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BikerProfileViewModel @Inject constructor(
    private val bikerSharedPreferences: BikerSharedPreferences
) : BaseViewModel() {
    private val profileViewState = MutableLiveData<ProfileViewState>()
    internal fun getUserProfileState(): LiveData<ProfileViewState> = profileViewState

    init {
        profileViewState.value = ProfileViewState.ProfileLoading
        getBikerProfile()
    }

    private fun getBikerProfile() {
        bikerSharedPreferences.getBikerProfile()?.let {
            profileViewState.postValue(
                ProfileViewState.ProfileLoaded(
                    BikerUIModel(
                        it.id,
                        it.name,
                        Uri.decode(it.profilePicture)
                    )
                )
            )
        } ?: profileViewState.postValue(ProfileViewState.ProfileError)
    }
}