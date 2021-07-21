package com.app.bikercopilot.bikerprofile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.app.bikercopilot.BaseFragment
import com.app.bikercopilot.R
import com.app.bikercopilot.auth.AuthViewModel
import com.app.bikercopilot.auth.UserAuthViewState
import com.app.bikercopilot.common.ext.observe
import com.app.bikercopilot.databinding.FragmentBikerProfileBinding
import com.app.bikercopilot.databinding.FragmentLoginBinding
import com.app.bikercopilot.ui.models.BikerUIModel
import com.app.bikercopilot.utils.displayImageBitmap
import com.app.bikercopilot.utils.displayImageBitmapCircle
import dagger.hilt.android.AndroidEntryPoint
import java.net.URLDecoder

@AndroidEntryPoint
class BikerProfileFragment : BaseFragment() {
    private lateinit var authViewModel: AuthViewModel
    private val bikerProfileViewModel: BikerProfileViewModel by viewModels()
    lateinit var fragmentLoginBinding: FragmentBikerProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_biker_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentBikerProfileBinding.bind(view)
        fragmentLoginBinding = binding
    }

    override fun onFragmentCreated() {
        super.onFragmentCreated()
        authViewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
        observe(bikerProfileViewModel.getUserProfileState(), ::onBikerProfileState)
        lifecycle.addObserver(bikerProfileViewModel)
        bikerProfileViewModel.getUserProfileState()
    }

    private fun onBikerProfileState(profileViewState: ProfileViewState?) {
        Log.wtf("onBikerProfileState",profileViewState.toString())
        when (profileViewState) {
            ProfileViewState.ProfileError -> {
            }
            is ProfileViewState.ProfileLoaded -> {
                bindProfile(profileViewState.biker)
            }
            ProfileViewState.ProfileLoading -> {
            }
        }
    }

    private fun bindProfile(biker: BikerUIModel) {
        fragmentLoginBinding.profileHeader.apply {
            imgBikerProfile.displayImageBitmapCircle(URLDecoder.decode(biker.profilePicture,"UTF-8"))
            nameBikerProfile.text = biker.name

        }
        fragmentLoginBinding.apply {
            btnSignOut.setOnClickListener {
                authViewModel.signOut()
            }
        }
    }



}