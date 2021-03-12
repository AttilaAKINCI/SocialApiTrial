package com.akinci.socialapitrial.feature.secure.di

import com.akinci.socialapitrial.BuildConfig
import com.akinci.socialapitrial.common.di.AppModule
import com.akinci.socialapitrial.common.storage.LocalPreferenceConfig
import com.akinci.socialapitrial.common.storage.Preferences
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer
import se.akerfeldt.okhttp.signpost.SigningInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SecureModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class SecureOkHttpClient

    @Provides
    @SecureOkHttpClient
    fun provideRestOkHttpClient(
            sharedPreferences: Preferences
    ) : OkHttpClient {
        val builder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            // debug logging activated
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BODY

            //add logging interceptor
            builder.addInterceptor(logger)
        }

        val oauthToken = sharedPreferences.getStoredTag(LocalPreferenceConfig.ACCESS_TOKEN)
        val oauthTokenSecret = sharedPreferences.getStoredTag(LocalPreferenceConfig.ACCESS_TOKEN_SECRET)

        val authSigner = OkHttpOAuthConsumer(BuildConfig.CONSUMER_KEY, BuildConfig.CONSUMER_SECRET)
        authSigner.setTokenWithSecret(oauthToken, oauthTokenSecret)
        return builder
                .addInterceptor(SigningInterceptor(authSigner))
                .readTimeout(100, TimeUnit.SECONDS)
                .connectTimeout(100, TimeUnit.SECONDS)
                .build()
    }

    @Provides
    fun provideMoshiConverterFactory(mosh: Moshi): MoshiConverterFactory = MoshiConverterFactory.create(mosh)

    @Singleton
    @Provides
    fun providesMoshi(): Moshi = Moshi.Builder().build()

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class SecureRetrofitClient

    @Provides
    @SecureRetrofitClient
    fun provideRetrofit(
            @SecureOkHttpClient okHttpClient: OkHttpClient,
            @AppModule.BaseURL baseURL: String,
            converter: MoshiConverterFactory
    ) : Retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .client(okHttpClient)
            .addConverterFactory(converter)
            .build()


}