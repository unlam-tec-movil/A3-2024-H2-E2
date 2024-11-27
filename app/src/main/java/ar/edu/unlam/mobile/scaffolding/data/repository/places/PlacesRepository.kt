package ar.edu.unlam.mobile.scaffolding.data.repository.places

import ar.edu.unlam.mobile.scaffolding.data.local.supermerkados.LocationDao
import ar.edu.unlam.mobile.scaffolding.data.local.supermerkados.LocationEntity
import ar.edu.unlam.mobile.scaffolding.data.local.supermerkados.PlaceDao
import ar.edu.unlam.mobile.scaffolding.data.local.supermerkados.PlaceEntity
import ar.edu.unlam.mobile.scaffolding.data.network.Geometry
import ar.edu.unlam.mobile.scaffolding.data.network.GooglePlacesApi
import ar.edu.unlam.mobile.scaffolding.data.network.Location
import ar.edu.unlam.mobile.scaffolding.data.network.PlaceResult
import ar.edu.unlam.mobile.scaffolding.data.network.PlacesResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlacesRepository(
    private val apiService: GooglePlacesApi,
    private val placeDao: PlaceDao,
    private val locationDao: LocationDao,
) {
    suspend fun getNearbyPlaces(
        location: String,
        radius: Int,
        type: String,
        apiKey: String,
        callback: (List<PlaceResult>?, String?) -> Unit,
    ) {
        // Intentar obtener lugares desde la base de datos
        val localPlaces = placeDao.getAllPlaces()
        if (localPlaces.isNotEmpty()) {
            // Si hay lugares en la base de datos, devolverlos
            callback(
                localPlaces.map { placeEntity ->
                    PlaceResult(
                        name = placeEntity.name,
                        vicinity = placeEntity.vicinity,
                        geometry = Geometry(Location(placeEntity.latitude, placeEntity.longitude)),
                    )
                },
                null,
            )
        } else {
            // Si no hay lugares, obtenerlos de la API
            // Llamada asíncrona con Retrofit
            apiService.getNearbyPlaces(location, radius, type, apiKey).enqueue(
                object : Callback<PlacesResponse> {
                    override fun onResponse(
                        call: Call<PlacesResponse>,
                        response: Response<PlacesResponse>,
                    ) {
                        if (response.isSuccessful) {
                            val places = response.body()?.results
                            places?.let {
                                // Guardar los lugares en la base de datos en un hilo adecuado
                                // Usamos Dispatchers.IO para operaciones de base de datos
                                CoroutineScope(Dispatchers.IO).launch {
                                    try {
                                        // Inserción de los lugares en la base de datos
                                        placeDao.insertPlaces(
                                            it.map { place ->
                                                val nombre = place.name ?: "Nombre no disponible"
                                                val vicinity = place.vicinity ?: "Dirección no disponible"
                                                PlaceEntity(
                                                    name = nombre,
                                                    vicinity = vicinity,
                                                    latitude = place.geometry.location.lat,
                                                    longitude = place.geometry.location.lng,
                                                )
                                            },
                                        )
                                        // Volver al hilo principal para llamar al callback
                                        withContext(Dispatchers.Main) {
                                            callback(it, null)
                                        }
                                    } catch (e: Exception) {
                                        // Manejo de errores
                                        withContext(Dispatchers.Main) {
                                            callback(null, "Error: ${e.message}")
                                        }
                                    }
                                }
                            }
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

    // Función suspensiva que obtiene la ubicación del usuario desde la base de datos

    // Obtener la ubicación desde la base de datos (asumiendo que LocationEntity usa android.location.Location)
    suspend fun getUserLocation(callback: (android.location.Location?) -> Unit) {
        val savedLocation = locationDao.getLocation() // Aquí, LocationEntity debería tener lat y lon
        callback(
            savedLocation?.let {
                android.location.Location("").apply {
                    latitude = it.latitude
                    longitude = it.longitude
                }
            },
        )
    }

    // Función suspensiva para guardar la ubicación del usuario en la base de datos

    // Guardar android.location.Location directamente en la base de datos
    suspend fun saveUserLocation(location: android.location.Location) {
        val locationEntity =
            LocationEntity(
                latitude = location.latitude,
                longitude = location.longitude,
            )
        locationDao.insertLocation(locationEntity)
    }
}
