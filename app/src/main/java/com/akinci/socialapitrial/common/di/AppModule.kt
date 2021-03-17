package com.akinci.socialapitrial.common.di

import android.content.Context
import com.akinci.socialapitrial.common.coroutine.CoroutineContextProvider
import com.akinci.socialapitrial.common.network.NetworkChecker
import com.akinci.socialapitrial.common.network.RestConfig
import com.akinci.socialapitrial.common.storage.LocalPreferences
import com.akinci.socialapitrial.common.storage.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // live as long as hole application
object AppModule {

    /** Coroutine context provider
     * START
     * **/
    @Provides
    @Singleton
    fun provideCoroutineContext() = CoroutineContextProvider()
    /** END **/

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


    /** Retrofit HILT Integrations
     * START
     * **/
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class BaseURL

    @Provides
    @BaseURL
    fun provideBaseUrl() = RestConfig.API_BASE_URL

}