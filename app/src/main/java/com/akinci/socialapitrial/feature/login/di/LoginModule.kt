package com.akinci.socialapitrial.feature.login.di

import android.content.Context
import android.content.SharedPreferences
import com.akinci.socialapitrial.BuildConfig
import com.akinci.socialapitrial.common.network.NetworkChecker
import com.akinci.socialapitrial.common.network.RestConfig
import com.akinci.socialapitrial.common.storage.Preferences
import com.akinci.socialapitrial.feature.login.data.api.LoginServiceDao
import com.akinci.socialapitrial.feature.login.repository.LoginRepository
import com.akinci.socialapitrial.feature.login.repository.LoginRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer
import se.akerfeldt.okhttp.signpost.SigningInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoginModule {

    /** Retrofit HILT Integrations
     * START
     * **/
    @Provides
    @Singleton
    @Named("LoginOkHttpClient")
    fun provideRestOkHttpClient(
        @ApplicationContext context: Context
    ) : OkHttpClient {
        val builder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            // debug logging activated
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BODY

            //add logging interceptor
            builder.addInterceptor(logger)
        }

        val authSigner = OkHttpOAuthConsumer(BuildConfig.CONSUMER_KEY, BuildConfig.CONSUMER_SECRET)
        return builder
            .addInterceptor(SigningInterceptor(authSigner))
            .readTimeout(100, TimeUnit.SECONDS)
            .connectTimeout(100, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @Named("StringResponseRetrofitClient")
    fun provideRetrofit(
        @Named("LoginOkHttpClient") okHttpClient: OkHttpClient,
        @Named("BaseURL") baseURL: String
    ) : Retrofit = Retrofit.Builder()
        .baseUrl(baseURL)
        .client(okHttpClient)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()


    @Provides
    @Singleton
    fun provideLoginServiceDao(
        @Named("StringResponseRetrofitClient") retrofit: Retrofit
    ): LoginServiceDao = retrofit.create(LoginServiceDao::class.java)

    @Provides
    @Singleton
    fun provideLoginRepository(
        loginServiceDao: LoginServiceDao,
        networkChecker: NetworkChecker
    ): LoginRepository = LoginRepositoryImpl(loginServiceDao, networkChecker)

    /** END **/

}