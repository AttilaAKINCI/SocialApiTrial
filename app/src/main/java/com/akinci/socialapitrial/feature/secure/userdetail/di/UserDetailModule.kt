package com.akinci.socialapitrial.feature.secure.userdetail.di

import com.akinci.socialapitrial.common.network.NetworkChecker
import com.akinci.socialapitrial.feature.secure.userdetail.data.api.UserDetailServiceDao
import com.akinci.socialapitrial.feature.secure.userdetail.repository.UserDetailRepository
import com.akinci.socialapitrial.feature.secure.userdetail.repository.UserDetailRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserDetailModule {

    @Provides
    @Singleton
    fun provideUserDetailServiceDao(
            @Named("SecureRetrofitClient") retrofit: Retrofit
    ): UserDetailServiceDao = retrofit.create(UserDetailServiceDao::class.java)

    @Provides
    @Singleton
    fun provideUserDetailRepository(
            userDetailServiceDao: UserDetailServiceDao,
            networkChecker: NetworkChecker
    ): UserDetailRepository = UserDetailRepositoryImpl(userDetailServiceDao, networkChecker)

}