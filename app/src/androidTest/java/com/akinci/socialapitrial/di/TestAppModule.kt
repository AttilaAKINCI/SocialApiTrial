package com.akinci.socialapitrial.di

import android.content.Context
import com.akinci.socialapitrial.common.storage.LocalPreferences
import com.akinci.socialapitrial.common.storage.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class TestLocalPreference

    @Provides
    @TestLocalPreference
    fun provideLocalPreferences(
        @ApplicationContext context: Context
    ) : Preferences = LocalPreferences(context)

}