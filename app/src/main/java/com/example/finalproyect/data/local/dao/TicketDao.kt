package com.example.finalproyect.data.local.dao



import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.finalproyect.data.local.entity.TicketEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TicketDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTicket(ticket: TicketEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTickets(tickets: List<TicketEntity>)

    @Update
    suspend fun updateTicket(ticket: TicketEntity)

    @Query("SELECT * FROM tickets WHERE id_ticket = :ticketId")
    suspend fun getTicketById(ticketId: Int): TicketEntity?

    @Query("SELECT * FROM tickets WHERE id_user = :userId ORDER BY created_at DESC")
    suspend fun getTicketsByUserId(userId: Int): List<TicketEntity>

    @Query("SELECT * FROM tickets WHERE id_user = :userId ORDER BY created_at DESC")
    fun getTicketsByUserIdFlow(userId: Int): Flow<List<TicketEntity>>

    @Query("""
        SELECT t.* FROM tickets t 
        INNER JOIN ticket_types tt ON t.id_ticket_type = tt.id_ticket_type 
        WHERE tt.id_event = :eventId AND t.id_user = :userId
    """)
    suspend fun getUserTicketForEvent(eventId: Int, userId: Int): TicketEntity?

    @Query("""
        SELECT t.* FROM tickets t 
        INNER JOIN ticket_types tt ON t.id_ticket_type = tt.id_ticket_type 
        WHERE tt.id_event = :eventId AND t.id_user = :userId
    """)
    fun getUserTicketForEventFlow(eventId: Int, userId: Int): Flow<TicketEntity?>

    @Query("SELECT * FROM tickets WHERE id_ticket_type = :ticketTypeId")
    suspend fun getTicketsByType(ticketTypeId: Int): List<TicketEntity>

    @Query("DELETE FROM tickets WHERE id_ticket = :ticketId")
    suspend fun deleteTicketById(ticketId: Int)

    @Query("DELETE FROM tickets WHERE id_user = :userId")
    suspend fun deleteTicketsByUserId(userId: Int)

    @Query("DELETE FROM tickets")
    suspend fun deleteAllTickets()
}
