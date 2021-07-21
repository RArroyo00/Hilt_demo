package com.app.bikercopilot.di

import android.content.Context
import com.app.bikercopilot.common.ResourceProvider
import com.app.bikercopilot.common.ResourceProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ResourceProviderModule {
    @Provides
    @Singleton
    fun provideResourceProvider(@ApplicationContext appContext: Context): ResourceProvider {
        return ResourceProviderImpl(appContext)
    }
}