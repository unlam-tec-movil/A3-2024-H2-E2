package ar.edu.unlam.mobile.scaffolding.ui.screens

import com.google.gson.annotations.SerializedName

data class PlacesResponse(
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
)
