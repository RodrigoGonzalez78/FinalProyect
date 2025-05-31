package com.example.finalproyect.data.local.dao

import androidx.room.*
import com.example.finalproyect.data.local.entity.LocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Query("SELECT * FROM locations")
    fun getAllLocations(): Flow<List<LocationEntity>>

    @Query("SELECT * FROM locations WHERE id = :locationId")
    fun getLocationById(locationId: Long): Flow<LocationEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: LocationEntity): Long

    @Update
    suspend fun updateLocation(location: LocationEntity)

    @Delete
    suspend fun deleteLocation(location: LocationEntity)
}