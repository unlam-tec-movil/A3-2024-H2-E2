package ar.edu.unlam.mobile.scaffolding.data.local.places

import android.content.Context
import android.net.ConnectivityManager
import ar.edu.unlam.mobile.scaffolding.ui.screens.PlaceResult

class PlaceRepository(
    private val placeDao: PlaceDao,
    private val context: Context,
) {
    suspend fun getPlaces(): List<PlaceEntity> =
        if (isInternetAvailable(context)) {
            val apiPlaces = fetchPlacesFromGoogle()
            placeDao.insertPlaces(
                apiPlaces.map {
                    PlaceEntity(
                        placeId = it.id,
                        name = it.name,
                        latitude = it.geometry.location.lat,
                        longitude = it.geometry.location.lng,
                        address = it.vicinity,
                    )
                },
            )
            apiPlaces.map {
                PlaceEntity(
                    placeId = it.id,
                    name = it.name,
                    latitude = it.geometry.location.lat,
                    longitude = it.geometry.location.lng,
                    address = it.vicinity,
                )
            }
        } else {
            placeDao.getAllPlaces()
        }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }

    private suspend fun fetchPlacesFromGoogle(): List<PlaceResult> {
        // Implementa la llamada a la API de Google Places
        return emptyList() // Reemplázalo con la lógica real
    }
}
