package com.example.finalproyect.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.finalproyect.data.local.entity.TicketTypeEntity

import kotlinx.coroutines.flow.Flow

@Dao
interface TicketTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTicketType(ticketType: TicketTypeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTicketTypes(ticketTypes: List<TicketTypeEntity>)

    @Update
    suspend fun updateTicketType(ticketType: TicketTypeEntity)

    @Query("SELECT * FROM ticket_types WHERE id_event = :eventId ORDER BY created_at ASC")
    suspend fun getTicketTypesByEventId(eventId: Int): List<TicketTypeEntity>

    @Query("SELECT * FROM ticket_types WHERE id_event = :eventId ORDER BY created_at ASC")
    fun getTicketTypesByEventIdFlow(eventId: Int): Flow<List<TicketTypeEntity>>

    @Query("SELECT * FROM ticket_types WHERE id_ticket_type = :ticketTypeId")
    suspend fun getTicketTypeById(ticketTypeId: Int): TicketTypeEntity?

    @Query("DELETE FROM ticket_types WHERE id_ticket_type = :ticketTypeId")
    suspend fun deleteTicketTypeById(ticketTypeId: Int)

    @Query("DELETE FROM ticket_types WHERE id_event = :eventId")
    suspend fun deleteTicketTypesByEventId(eventId: Int)

    @Query("DELETE FROM ticket_types")
    suspend fun deleteAllTicketTypes()
}
