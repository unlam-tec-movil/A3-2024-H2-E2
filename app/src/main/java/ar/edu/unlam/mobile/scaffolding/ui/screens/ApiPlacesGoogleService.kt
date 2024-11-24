package ar.edu.unlam.mobile.scaffolding.ui.screens

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiPlacesGoogleService {
    private const val BASE_URL = "https://maps.googleapis.com/maps/api/place/"

    val apiService: GooglePlacesApi by lazy {
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GooglePlacesApi::class.java)
    }
}

/*import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApiService {
    @GET("maps/api/place/nearbysearch/json")
    suspend fun getNearbyPlaces(
        @Query("location") location: String,
        @Query("radius") radius: Int,
        @Query("type") type: String = "supermarket",
        @Query("key") apiKey: String,
    ): PlaceResponse
}

object RetrofitInstance {
    private const val BASE_URL = "https://maps.googleapis.com/"

    val api: PlacesApiService by lazy {
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PlacesApiService::class.java)
    }
}
*/
