package ar.edu.unlam.mobile.scaffolding.data.network

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
