package com.akinci.socialapitrial.feature.secure.user.di

import com.akinci.socialapitrial.common.network.NetworkChecker
import com.akinci.socialapitrial.feature.secure.user.data.api.UserServiceDao
import com.akinci.socialapitrial.feature.secure.user.repository.UserRepository
import com.akinci.socialapitrial.feature.secure.user.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Provides
    @Singleton
    fun provideUserDetailServiceDao(
            @Named("SecureRetrofitClient") retrofit: Retrofit
    ): UserServiceDao = retrofit.create(UserServiceDao::class.java)

    @Provides
    @Singleton
    fun provideUserDetailRepository(
            userServiceDao: UserServiceDao,
            networkChecker: NetworkChecker
    ): UserRepository = UserRepositoryImpl(userServiceDao, networkChecker)
}