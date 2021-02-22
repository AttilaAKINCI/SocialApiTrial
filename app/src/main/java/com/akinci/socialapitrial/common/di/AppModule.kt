package com.akinci.socialapitrial.common.di

import android.content.Context
import com.akinci.socialapitrial.common.network.NetworkChecker
import com.akinci.socialapitrial.common.storage.LocalPreferences
import com.akinci.socialapitrial.common.storage.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // live as long as hole application
object AppModule {

    /** Network Connection Checker Integration
     * START
     * **/
    @Provides
    @Singleton
    fun provideNetworkChecker(@ApplicationContext context: Context) = NetworkChecker(context)

    /** Shared Preferences Integration
     * START
     * **/
    @Provides
    @Singleton
    fun provideLocalPreferences(@ApplicationContext context: Context) : Preferences = LocalPreferences(context)
    /** END **/

}