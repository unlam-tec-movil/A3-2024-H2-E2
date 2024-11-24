package ar.edu.unlam.mobile.scaffolding.ui.screens
import android.content.Context
import ar.edu.unlam.mobile.scaffolding.data.local.places.PlaceDao
import ar.edu.unlam.mobile.scaffolding.data.local.places.PlaceEntity

class PlacesRepository(
    private val apiService: GooglePlacesApi,
    private val placeDao: PlaceDao,
    private val context: Context, // Agregado para el acceso a la base de datos local
) {
    // Método suspend para obtener lugares, ya sea desde la API o localmente
    suspend fun getPlaces(
        location: String,
        radius: Int,
        type: String?,
    ): List<PlaceEntity> =
        if (isInternetAvailable(context)) {
            // Si hay conexión a internet, usa la API de Google Places
            val apiPlaces = fetchPlacesFromGoogle(location, radius, type)
            // Guarda los resultados en la base de datos local
            placeDao.insertPlaces(
                apiPlaces.map {
                    PlaceEntity(
                        placeId = it.id ?: "Unknown ID",
                        name = it.name ?: "Unknown Name",
                        latitude = it.geometry.location.lat,
                        longitude = it.geometry.location.lng,
                        address = it.vicinity ?: "Unknown Address",
                    )
                },
            )
            // Devuelve los datos obtenidos desde la API
            apiPlaces.map {
                PlaceEntity(
                    placeId = it.id ?: "Unknown ID",
                    name = it.name ?: "Unknown Name",
                    latitude = it.geometry.location.lat,
                    longitude = it.geometry.location.lng,
                    address = it.vicinity ?: "Unknown Address",
                )
            }
        } else {
            // Si no hay conexión a internet, usa los datos almacenados en la base de datos local
            placeDao.getAllPlaces()
        }

    // Método para obtener datos directamente desde la API
    private suspend fun fetchPlacesFromGoogle(
        location: String,
        radius: Int,
        type: String?,
    ): List<PlaceResult> {
        val response = apiService.getNearbyPlaces(location, radius, type).execute()
        if (response.isSuccessful) {
            return response.body()?.results ?: emptyList()
        } else {
            throw Exception("Error al obtener datos de la API: ${response.message()}")
        }
    }
}

/*class PlacesRepository(
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
