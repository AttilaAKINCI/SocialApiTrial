package com.akinci.socialapitrial.feature.login.di

import android.content.Context
import com.akinci.socialapitrial.BuildConfig
import com.akinci.socialapitrial.common.di.AppModule
import com.akinci.socialapitrial.common.network.NetworkChecker
import com.akinci.socialapitrial.feature.login.data.api.LoginServiceDao
import com.akinci.socialapitrial.feature.login.repository.LoginRepository
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
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoginModule {

    /** Retrofit HILT Integrations
     * START
     * **/
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class LoginOkHttpClient

    @Provides
    @Singleton
    @LoginOkHttpClient
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

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class StringResponseRetrofitClient

    @Provides
    @Singleton
    @StringResponseRetrofitClient
    fun provideRetrofit(
        @LoginOkHttpClient okHttpClient: OkHttpClient,
        @AppModule.BaseURL baseURL: String
    ) : Retrofit = Retrofit.Builder()
        .baseUrl(baseURL)
        .client(okHttpClient)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideLoginServiceDao(
        @StringResponseRetrofitClient retrofit: Retrofit
    ): LoginServiceDao = retrofit.create(LoginServiceDao::class.java)

    @Provides
    @Singleton
    fun provideLoginRepository(
        loginServiceDao: LoginServiceDao,
        networkChecker: NetworkChecker
    ) = LoginRepository(loginServiceDao, networkChecker)

    /** END **/

}