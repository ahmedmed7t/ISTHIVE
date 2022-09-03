package com.isthive.ist.app.di

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.contentValuesOf
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.isthive.ist.BuildConfig
import com.isthive.ist.app.api.ApiService
import com.isthive.ist.app.models.Constants
import com.isthive.ist.app.models.ErrorInterceptor
import com.isthive.ist.app.models.ExpirationTokenInterceptor
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class AppModule {

    @Provides
    fun provideExpirationTokenInterceptor(
        sharedPreferences: SharedPreferences
    ): ExpirationTokenInterceptor {
        return ExpirationTokenInterceptor(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(expirationTokenInterceptor: ExpirationTokenInterceptor,
                            @ApplicationContext appContext: Context): OkHttpClient =
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS)
            OkHttpClient.Builder()
                .addInterceptor(expirationTokenInterceptor)
                .addInterceptor(ErrorInterceptor())
                .addInterceptor(
                    ChuckerInterceptor.Builder(context = appContext)
                        .collector(ChuckerCollector(appContext))
                        .maxContentLength(250000L)
                        .redactHeaders(emptySet())
                        .alwaysReadResponseBody(false)
                        .build()
                )
                .addInterceptor(loggingInterceptor)
                .build()
        } else OkHttpClient
            .Builder()
            .addInterceptor(ErrorInterceptor())
            .addInterceptor(expirationTokenInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext appContext: Context): SharedPreferences =
        appContext.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
}