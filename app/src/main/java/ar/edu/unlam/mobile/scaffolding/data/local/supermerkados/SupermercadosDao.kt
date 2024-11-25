package ar.edu.unlam.mobile.scaffolding.data.local.supermerkados

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlaceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaces(places: List<PlaceEntity>)

    @Query("SELECT * FROM places")
    suspend fun getAllPlaces(): List<PlaceEntity>
}

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: LocationEntity)

    @Query("SELECT * FROM locations LIMIT 1")
    suspend fun getLocation(): LocationEntity?
}
