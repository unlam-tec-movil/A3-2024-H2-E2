package ar.edu.unlam.mobile.scaffolding.data.network

data class PlacesResponse(
    val results: List<PlaceResult>,
)

data class PlaceResult(
    val name: String?,
    val vicinity: String?, // Dirección
    val geometry: Geometry,
)

data class Geometry(
    val location: Location,
)

data class Location(
    val lat: Double,
    val lng: Double,
)
