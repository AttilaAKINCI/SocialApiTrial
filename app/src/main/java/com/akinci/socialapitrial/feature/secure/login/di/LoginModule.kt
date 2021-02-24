package com.akinci.socialapitrial.feature.secure.login.di

import com.akinci.socialapitrial.common.network.NetworkChecker
import com.akinci.socialapitrial.feature.secure.login.data.api.LoginServiceDao
import com.akinci.socialapitrial.feature.secure.login.repository.LoginRepository
import com.akinci.socialapitrial.feature.secure.login.repository.LoginRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoginModule {

    @Provides
    @Singleton
    fun provideLoginServiceDao(
            @Named("SecureRetrofitClient") retrofit: Retrofit
    ): LoginServiceDao = retrofit.create(LoginServiceDao::class.java)

    @Provides
    @Singleton
    fun provideLoginRepository(
            loginServiceDao: LoginServiceDao,
            networkChecker: NetworkChecker
    ): LoginRepository = LoginRepositoryImpl(loginServiceDao, networkChecker)

}