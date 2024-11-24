package ar.edu.unlam.mobile.scaffolding.data.local.places

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "places")
data class PlaceEntity(
    @PrimaryKey val placeId: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val address: String,
)
