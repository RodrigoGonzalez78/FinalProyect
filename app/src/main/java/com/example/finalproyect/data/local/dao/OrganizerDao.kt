package com.example.finalproyect.data.local.dao

import androidx.room.*
import com.example.finalproyect.data.local.entity.OrganizerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrganizerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrganizer(organizer: OrganizerEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrganizers(organizers: List<OrganizerEntity>)

    @Update
    suspend fun updateOrganizer(organizer: OrganizerEntity)

    @Query("SELECT * FROM organizers WHERE id_event = :eventId ORDER BY id_rol ASC, created_at ASC")
    suspend fun getOrganizersByEventId(eventId: Int): List<OrganizerEntity>

    @Query("SELECT * FROM organizers WHERE id_event = :eventId ORDER BY id_rol ASC, created_at ASC")
    fun getOrganizersByEventIdFlow(eventId: Int): Flow<List<OrganizerEntity>>

    @Query("SELECT * FROM organizers WHERE id_organizer = :organizerId")
    suspend fun getOrganizerById(organizerId: Int): OrganizerEntity?

    @Query("SELECT * FROM organizers WHERE id_event = :eventId AND id_user = :userId")
    suspend fun getOrganizerByEventAndUser(eventId: Int, userId: Int): OrganizerEntity?

    @Query("SELECT * FROM organizers WHERE id_event = :eventId AND id_rol = 1")
    suspend fun getMainAdminByEvent(eventId: Int): OrganizerEntity?

    @Query("DELETE FROM organizers WHERE id_organizer = :organizerId")
    suspend fun deleteOrganizerById(organizerId: Int)

    @Query("DELETE FROM organizers WHERE id_event = :eventId")
    suspend fun deleteOrganizersByEventId(eventId: Int)

    @Query("DELETE FROM organizers")
    suspend fun deleteAllOrganizers()
}