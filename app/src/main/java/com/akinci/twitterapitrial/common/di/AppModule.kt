package com.akinci.twitterapitrial.common.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class) // live as long as hole application
object AppModule {

}