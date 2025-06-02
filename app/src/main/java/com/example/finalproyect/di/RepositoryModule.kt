package com.example.finalproyect.di

import com.example.finalproyect.data.repository.AuthRepositoryImpl
import com.example.finalproyect.data.repository.EventRepositoryImpl
import com.example.finalproyect.data.repository.UploadRepositoryImpl
import com.example.finalproyect.domain.repository.AuthRepository
import com.example.finalproyect.domain.repository.EventRepository
import com.example.finalproyect.domain.repository.UploadRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindEventRepository(
        eventRepositoryImpl: EventRepositoryImpl
    ): EventRepository

    @Binds
    @Singleton
    abstract fun bindUploadRepository(
        uploadRepositoryImpl: UploadRepositoryImpl
    ): UploadRepository
}
