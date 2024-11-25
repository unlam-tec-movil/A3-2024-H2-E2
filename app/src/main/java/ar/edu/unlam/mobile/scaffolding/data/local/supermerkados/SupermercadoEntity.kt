package ar.edu.unlam.mobile.scaffolding.data.local.supermerkados

import androidx.room.Entity
import androidx.room.PrimaryKey

/*Necesitamos crear una entidad PlaceEntity para almacenar la información de los
 supermercados, y una entidad LocationEntity para las coordenadas geográficas.*/

@Entity(tableName = "places")
data class PlaceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String?,
    val vicinity: String?,
    val latitude: Double,
    val longitude: Double,
)

@Entity(tableName = "locations")
data class LocationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val latitude: Double,
    val longitude: Double,
)
