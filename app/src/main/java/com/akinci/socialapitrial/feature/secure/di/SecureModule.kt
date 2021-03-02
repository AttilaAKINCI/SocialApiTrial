package com.akinci.socialapitrial.feature.secure.di

import android.content.Context
import com.akinci.socialapitrial.BuildConfig
import com.akinci.socialapitrial.common.storage.LocalPreferenceConfig
import com.akinci.socialapitrial.common.storage.Preferences
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer
import se.akerfeldt.okhttp.signpost.SigningInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SecureModule {

    @Provides
    @Singleton
    @Named("SecureOkHttpClient")
    fun provideRestOkHttpClient(
            @ApplicationContext context: Context, //TODO context silinebilir
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

    //TODO Named yerine qualifier kullanabilirsin
    /*
    * @Qualifier
      @Retention(AnnotationRetention.RUNTIME)
      annotation class SecureRetrofitClient
    * */
    @Provides
    @Singleton
    @Named("SecureRetrofitClient")
    fun provideRetrofit(
            @Named("SecureOkHttpClient") okHttpClient: OkHttpClient,
            @Named("BaseURL") baseURL: String,
            converter: MoshiConverterFactory
    ) : Retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .client(okHttpClient)
            .addConverterFactory(converter)
            .build()


}

