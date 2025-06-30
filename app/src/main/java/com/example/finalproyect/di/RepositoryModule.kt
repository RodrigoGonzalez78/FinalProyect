package com.example.finalproyect.di

import com.example.finalproyect.data.repository.AuthRepositoryImpl
import com.example.finalproyect.data.repository.EventRepositoryImpl
import com.example.finalproyect.data.repository.OrganizerRepositoryImpl
import com.example.finalproyect.data.repository.TicketRepositoryImpl
import com.example.finalproyect.data.repository.TicketScanRepositoryImpl
import com.example.finalproyect.data.repository.TicketTypeRepositoryImpl
import com.example.finalproyect.data.repository.UploadRepositoryImpl
import com.example.finalproyect.data.repository.UserRepositoryImpl
import com.example.finalproyect.domain.repository.AuthRepository
import com.example.finalproyect.domain.repository.EventRepository
import com.example.finalproyect.domain.repository.OrganizerRepository
import com.example.finalproyect.domain.repository.TicketRepository
import com.example.finalproyect.domain.repository.TicketScanRepository
import com.example.finalproyect.domain.repository.TicketTypeRepository
import com.example.finalproyect.domain.repository.UploadRepository
import com.example.finalproyect.domain.repository.UserRepository
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

    @Binds
    @Singleton
    abstract fun bindTicketTypeRepository(
        ticketTypeRepositoryImpl: TicketTypeRepositoryImpl
    ): TicketTypeRepository

    @Binds
    @Singleton
    abstract fun bindTicketRepository(
        ticketRepositoryImpl: TicketRepositoryImpl
    ): TicketRepository


    @Binds
    abstract fun bindTicketScanRepository(
        ticketScanRepositoryImpl: TicketScanRepositoryImpl
    ): TicketScanRepository

    @Binds
    @Singleton
    abstract fun bindOrganizerRepository(
        organizerRepositoryImpl: OrganizerRepositoryImpl
    ): OrganizerRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository
}
