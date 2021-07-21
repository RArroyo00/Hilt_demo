package com.app.bikercopilot.bikerprofile

import com.app.bikercopilot.domain.Biker

interface BikerProfileStore {
    fun setBikerProfile(biker: Biker)

    fun getBikerProfile(): Biker?

    fun clearData()
}