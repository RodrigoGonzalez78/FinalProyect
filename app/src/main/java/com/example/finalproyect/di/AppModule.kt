package com.example.finalproyect.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.example.finalproyect.data.local.dao.UserDao
import com.example.finalproyect.data.local.database.AppDatabase
import com.example.finalproyect.data.remote.ApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides @Singleton
    fun provideGson(): Gson =
        GsonBuilder().create()

    @Provides @Singleton
    fun provideRetrofit(gson: Gson): Retrofit =
        Retrofit.Builder()
            .baseUrl(ApiService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    @Provides @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Provides @Singleton
    fun provideAppDatabase(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "app.db").build()

    @Provides @Singleton
    fun provideUserDao(db: AppDatabase): UserDao =
        db.userDao()

    @Provides @Singleton
    fun provideDataStore(@ApplicationContext ctx: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create { ctx.preferencesDataStoreFile("settings") }
}
