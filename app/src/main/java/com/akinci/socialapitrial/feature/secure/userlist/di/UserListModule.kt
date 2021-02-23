package com.akinci.socialapitrial.feature.secure.userlist.di

import com.akinci.socialapitrial.common.network.NetworkChecker
import com.akinci.socialapitrial.common.storage.Preferences
import com.akinci.socialapitrial.feature.secure.userlist.data.api.UserListServiceDao
import com.akinci.socialapitrial.feature.secure.userlist.repository.UserListRepository
import com.akinci.socialapitrial.feature.secure.userlist.repository.UserListRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserListModule {

    @Provides
    @Singleton
    fun provideUserListServiceDao(
            @Named("SecureRetrofitClient") retrofit: Retrofit
    ): UserListServiceDao = retrofit.create(UserListServiceDao::class.java)

    @Provides
    @Singleton
    fun provideUserListRepository(
            userListServiceDao: UserListServiceDao,
            networkChecker: NetworkChecker,
            sharedPreferences: Preferences
    ): UserListRepository = UserListRepositoryImpl(userListServiceDao, networkChecker, sharedPreferences)
}