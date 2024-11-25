package ar.edu.unlam.mobile.scaffolding.ui.screens

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

/*data class PlacesResponse(
    @SerializedName("results") val results: List<PlaceResult>,
)

data class PlaceResult(
    @SerializedName("geometry") val geometry: Geometry,
    @SerializedName("name") val name: String,
)

data class Geometry(
    @SerializedName("location") val location: Location,
)

data class Location(
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double,
)*/
