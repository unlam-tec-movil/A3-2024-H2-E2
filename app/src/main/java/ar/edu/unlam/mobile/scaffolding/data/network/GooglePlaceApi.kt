package ar.edu.unlam.mobile.scaffolding.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GooglePlacesApi {
    @GET("nearbysearch/json")
    fun getNearbyPlaces(
        @Query("location") location: String, // Coordenadas: "lat,lng"
        @Query("radius") radius: Int, // Radio en metros
        @Query("type") type: String, // Tipo de lugar (e.g., "supermarket")
        @Query("key") apiKey: String, // Clave de la API
    ): Call<PlacesResponse>
}
