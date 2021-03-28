package com.akinci.socialapitrial.feature.secure.login.di

import com.akinci.socialapitrial.common.network.NetworkChecker
import com.akinci.socialapitrial.feature.secure.di.SecureModule
import com.akinci.socialapitrial.feature.secure.login.data.api.LoginServiceDao
import com.akinci.socialapitrial.feature.secure.login.repository.LoginRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object LoginModule {

    @Provides
    fun provideLoginServiceDao(
            @SecureModule.SecureRetrofitClient retrofit: Retrofit
    ): LoginServiceDao = retrofit.create(LoginServiceDao::class.java)

    @Provides
    fun provideLoginRepository(
            loginServiceDao: LoginServiceDao,
            networkChecker: NetworkChecker
    ) = LoginRepository(loginServiceDao, networkChecker)

}