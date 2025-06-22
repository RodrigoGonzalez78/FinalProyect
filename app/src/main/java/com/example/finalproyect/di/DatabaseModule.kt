package com.example.finalproyect.di

import android.content.Context
import androidx.room.Room
import com.example.finalproyect.data.local.dao.EventDao
import com.example.finalproyect.data.local.dao.LocationDao
import com.example.finalproyect.data.local.dao.OrganizerDao
import com.example.finalproyect.data.local.dao.TicketDao
import com.example.finalproyect.data.local.dao.TicketTypeDao
import com.example.finalproyect.data.local.dao.UserDao
import com.example.finalproyect.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        )
            .fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides
    @Singleton
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }

    @Provides
    @Singleton
    fun provideEventDao(appDatabase: AppDatabase): EventDao {
        return appDatabase.eventDao()
    }

    @Provides
    @Singleton
    fun provideLocationDao(appDatabase: AppDatabase): LocationDao {
        return appDatabase.locationDao()
    }

    @Provides
    @Singleton
    fun provideOrganizerDao(database: AppDatabase): OrganizerDao {
        return database.organizerDao()
    }

    @Provides
    @Singleton
    fun provideTicketTypeDao(database: AppDatabase): TicketTypeDao {
        return database.ticketTypeDao()
    }

    @Provides
    @Singleton
    fun provideTicketDao(database: AppDatabase): TicketDao {
        return database.ticketDao()
    }
}