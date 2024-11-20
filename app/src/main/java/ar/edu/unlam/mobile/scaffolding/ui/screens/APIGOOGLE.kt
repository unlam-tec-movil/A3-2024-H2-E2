package ar.edu.unlam.mobile.scaffolding.ui.screens

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApiService {
    @GET("place/nearbysearch/json")
    suspend fun getNearbyPlaces(
        @Query("location") location: String,
        @Query("radius") radius: Int = 1000,
        @Query("type") type: String = "supermarket",
        @Query("key") apiKey: String,
    ): PlacesResponse
}

object RetrofitInstance {
    private const val BASE_URL = "https://maps.googleapis.com/maps/api/"
    val api: PlacesApiService by lazy {
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PlacesApiService::class.java)
    }
}
