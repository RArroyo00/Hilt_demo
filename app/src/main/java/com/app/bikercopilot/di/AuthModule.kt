package com.app.bikercopilot.di

import com.app.bikercopilot.auth.AuthenticationManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthManager(): AuthenticationManager {
        return AuthenticationManager()
    }
}