package com.app.bikercopilot.di

import android.content.Context
import com.app.bikercopilot.bikerprofile.BikerSharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object SharedPrefsModule {

    @Provides
    @Singleton
    fun provideBikerSharedPreferences(@ApplicationContext appContext: Context): BikerSharedPreferences {
        return BikerSharedPreferences(appContext)
    }

}