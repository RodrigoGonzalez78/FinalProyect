package com.example.finalproyect.data.local.dao


import androidx.room.*
import com.example.finalproyect.data.local.entity.EventEntity
import com.example.finalproyect.data.local.entity.EventWithLocation

import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<EventEntity>)

    @Query("SELECT * FROM events WHERE id_event = :eventId")
    fun getEventById(eventId: Int): Flow<EventEntity>

    @Query("SELECT * FROM events WHERE is_public = 1 ORDER BY created_at DESC LIMIT :size OFFSET :offset")
    suspend fun getPublicEvents(size: Int, offset: Int): List<EventEntity>

    @Query("SELECT COUNT(*) FROM events WHERE is_public = 1")
    suspend fun getPublicEventsCount(): Int

    @Query("SELECT * FROM events WHERE user_id = :userId ORDER BY created_at DESC LIMIT :size OFFSET :offset")
    suspend fun getEventsByUserId(userId: String, size: Int, offset: Int): List<EventEntity>

    @Query("SELECT COUNT(*) FROM events WHERE user_id = :userId")
    suspend fun getEventsCountByUserId(userId: String): Int

    @Query("SELECT * FROM events WHERE name LIKE '%' || :searchName || '%' AND is_public = 1 ORDER BY created_at DESC LIMIT :size OFFSET :offset")
    suspend fun searchPublicEvents(searchName: String, size: Int, offset: Int): List<EventEntity>

    @Query("SELECT COUNT(*) FROM events WHERE name LIKE '%' || :searchName || '%' AND is_public = 1")
    suspend fun getPublicEventsCount(searchName: String): Int

    @Query("DELETE FROM events WHERE id_event = :eventId")
    suspend fun deleteEventById(eventId: Int)
}
