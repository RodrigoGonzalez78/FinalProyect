package com.example.finalproyect.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository
}