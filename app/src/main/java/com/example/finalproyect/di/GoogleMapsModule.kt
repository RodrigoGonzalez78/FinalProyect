package com.example.finalproyect.di

import android.content.Context
import com.example.finalproyect.data.remote.api.GoogleMapsApi
import com.example.finalproyect.data.repository.GoogleMapsRepositoryImpl
import com.example.finalproyect.domain.repository.GoogleMapsRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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
object GoogleMapsModule {

    @Provides
    @Singleton
    fun provideGoogleMapsApi(): GoogleMapsApi {
        return Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GoogleMapsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGoogleMapsRepository(
        googleMapsApi: GoogleMapsApi
    ): GoogleMapsRepository {
        return GoogleMapsRepositoryImpl(googleMapsApi)
    }

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(
        @ApplicationContext context: Context
    ): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }
}