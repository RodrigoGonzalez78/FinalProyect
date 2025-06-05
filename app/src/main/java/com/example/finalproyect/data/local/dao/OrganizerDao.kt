package com.example.finalproyect.data.local.dao

import androidx.room.*
import com.example.finalproyect.data.local.entity.OrganizerEntity

@Dao
interface OrganizerDao {

    @Query("SELECT * FROM organizers WHERE id_event = :eventId")
    suspend fun getOrganizersByEventId(eventId: Int): List<OrganizerEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrganizers(organizers: List<OrganizerEntity>)

    @Query("DELETE FROM organizers WHERE id_event = :eventId")
    suspend fun deleteOrganizersByEventId(eventId: Int)

    @Query("DELETE FROM organizers")
    suspend fun deleteAllOrganizers()
}