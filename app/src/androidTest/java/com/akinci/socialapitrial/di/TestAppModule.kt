package com.akinci.socialapitrial.di

import android.content.Context
import com.akinci.socialapitrial.common.storage.LocalPreferences
import com.akinci.socialapitrial.common.storage.Preferences
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