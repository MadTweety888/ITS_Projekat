package com.example.wherekiddo.repository.di

import com.example.wherekiddo.repository.implementations.AuthRepository
import com.example.wherekiddo.repository.implementations.TrackingRepository
import com.example.wherekiddo.repository.implementations.UsersDataRepository
import com.example.wherekiddo.repository.interactors.AuthInteractor
import com.example.wherekiddo.repository.interactors.TrackingInteractor
import com.example.wherekiddo.repository.interactors.UsersDataInteractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
open class RepoModule {

    @Provides
    @Singleton
    fun provideAuthRepository(): AuthInteractor { return AuthRepository() }

    @Provides
    @Singleton
    fun provideUsersDataRepository(): UsersDataInteractor { return UsersDataRepository() }

    @Provides
    @Singleton
    fun provideTrackingRepository(): TrackingInteractor { return TrackingRepository() }
}