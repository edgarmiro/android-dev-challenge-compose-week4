package com.example.androiddevchallenge.ui.screens.di

import com.example.androiddevchallenge.data.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object MainModule {

    @Provides
    fun provideRepository() = MainRepository()
}