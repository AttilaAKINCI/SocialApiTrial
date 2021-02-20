package com.akinci.twitterapitrial.di

import android.content.Context
import com.akinci.twitterapitrial.common.storage.LocalPreferences
import com.akinci.twitterapitrial.common.storage.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Named("test-localPreference")
    fun provideLocalPreferences(
        @ApplicationContext context: Context
    ) : Preferences = LocalPreferences(context)

}