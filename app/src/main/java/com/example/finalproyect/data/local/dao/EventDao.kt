package com.example.finalproyect.data.local.dao


import androidx.room.*
import com.example.finalproyect.data.local.entity.EventEntity
import com.example.finalproyect.data.local.entity.EventWithLocation

import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Transaction
    @Query("SELECT * FROM events ORDER BY date DESC")
    fun getAllEvents(): Flow<List<EventWithLocation>>

    @Transaction
    @Query("SELECT * FROM events WHERE id = :eventId")
    fun getEventById(eventId: Long): Flow<EventWithLocation?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventEntity): Long

    @Update
    suspend fun updateEvent(event: EventEntity)

    @Delete
    suspend fun deleteEvent(event: EventEntity)
}
