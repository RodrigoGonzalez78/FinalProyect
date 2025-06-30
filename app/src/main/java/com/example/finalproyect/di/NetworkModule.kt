package com.example.finalproyect.di

import com.example.finalproyect.data.remote.api.AuthApi
import com.example.finalproyect.data.remote.api.EventApi
import com.example.finalproyect.data.remote.api.OrganizerApi
import com.example.finalproyect.data.remote.api.TicketApi
import com.example.finalproyect.data.remote.api.TicketScanApi
import com.example.finalproyect.data.remote.api.TicketTypeApi
import com.example.finalproyect.data.remote.api.UploadApi
import com.example.finalproyect.data.remote.api.UserApi
import com.example.finalproyect.utils.AuthInterceptor
import com.example.finalproyect.utils.Constants
import com.example.finalproyect.utils.PreferenceManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(preferenceManager: PreferenceManager): AuthInterceptor {
        return AuthInterceptor(preferenceManager)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideEventApi(retrofit: Retrofit): EventApi {
        return retrofit.create(EventApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUploadApi(retrofit: Retrofit): UploadApi {
        return retrofit.create(UploadApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTicketTypeApi(retrofit: Retrofit): TicketTypeApi {
        return retrofit.create(TicketTypeApi::class.java)
    }

    @Provides
    @Singleton
    fun provideOrganizerApi(retrofit: Retrofit): OrganizerApi {
        return retrofit.create(OrganizerApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTicketApi(retrofit: Retrofit): TicketApi {
        return retrofit.create(TicketApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }
    @Provides
    @Singleton
    fun provideTicketScanApi(retrofit: Retrofit): TicketScanApi {
        return retrofit.create(TicketScanApi::class.java)
    }
}
