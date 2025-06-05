package com.example.finalproyect.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.finalproyect.data.local.dao.EventDao
import com.example.finalproyect.data.local.dao.LocationDao
import com.example.finalproyect.data.local.dao.OrganizerDao
import com.example.finalproyect.data.local.dao.UserDao
import com.example.finalproyect.data.local.entity.EventEntity
import com.example.finalproyect.data.local.entity.LocationEntity
import com.example.finalproyect.data.local.entity.OrganizerEntity
import com.example.finalproyect.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        EventEntity::class,
        LocationEntity::class,
        OrganizerEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun eventDao(): EventDao
    abstract fun locationDao(): LocationDao
    abstract fun organizerDao(): OrganizerDao
}