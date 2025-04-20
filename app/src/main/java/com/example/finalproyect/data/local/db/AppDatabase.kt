package com.example.finalproyect.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.finalproyect.data.local.dao.UserDao
import com.example.finalproyect.data.local.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1)
@TypeConverters(DateConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}