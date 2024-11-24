package ar.edu.unlam.mobile.scaffolding.ui.screens

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlacesRepository(
    private val apiService: GooglePlacesApi,
) {
    fun getNearbyPlaces(
        location: String,
        radius: Int,
        type: String?,
        apiKey: String,
        callback: (List<PlaceResult>?, String?) -> Unit,
    ) {
        if (type != null) {
            apiService.getNearbyPlaces(location, radius, type, apiKey).enqueue(
                object : Callback<PlacesResponse> {
                    override fun onResponse(
                        call: Call<PlacesResponse>,
                        response: Response<PlacesResponse>,
                    ) {
                        if (response.isSuccessful) {
                            val places = response.body()?.results
                            callback(places, null)
                        } else {
                            callback(null, "Error: ${response.message()}")
                        }
                    }

                    override fun onFailure(
                        call: Call<PlacesResponse>,
                        t: Throwable,
                    ) {
                        callback(null, "Failure: ${t.message}")
                    }
                },
            )
        }
    }
}
/*
class PlacesRepository(
    private val api: GooglePlacesApi,
) {
    /*suspend fun getSupermarkets(
        lat: Double,
        lng: Double,
        apiKey: String,
    ): List<Place> {
        val location = "$lat,$lng"
        val radius = 1000 // 1 km
        val type = "supermarket"
        return api.getNearbyPlaces(location, radius, type, apiKey).results
    }*/
}*/
