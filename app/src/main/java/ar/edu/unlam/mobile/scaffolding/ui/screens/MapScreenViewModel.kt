package ar.edu.unlam.mobile.scaffolding.ui.screens

import android.annotation.SuppressLint
import android.app.Application
import android.location.Location
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unlam.mobile.scaffolding.data.local.places.PlaceEntity
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MapScreenViewModel(
    application: Application,
    private val repository: PlacesRepository =
        PlacesRepository(
            apiService = ApiPlacesGoogleService.apiService, // Usa el objeto directamente
            placeDao = PlaceDatabase.getInstance(application).placeDao(),
            context = application.applicationContext,
        ), // Repositorio combinado
) : AndroidViewModel(application) {
    private val _userLocation = MutableStateFlow<Location?>(null)
    val userLocation: StateFlow<Location?> = _userLocation

    private val _supermarkets = MutableStateFlow<List<PlaceEntity>>(emptyList()) // Usa PlaceEntity
    val supermarkets: StateFlow<List<PlaceEntity>> = _supermarkets

    @SuppressLint("StaticFieldLeak")
    private val context = application.applicationContext

    init {
        getUserLocation()
    }

    @SuppressLint("MissingPermission")
    private fun getUserLocation() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplication())
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                _userLocation.value = location
                fetchSupermarkets(location) // Llama a la nueva función combinada
            } else {
                Log.e("MapScreenViewModel", "No se pudo obtener la ubicación del usuario.")
            }
        }
    }

    // Método para obtener supermercados (usa el repositorio combinado)
    private fun fetchSupermarkets(location: Location) {
        val locationString = "${location.latitude},${location.longitude}"
        val radius = 1000 // Radio de búsqueda en metros
        val type = "supermarket"

        viewModelScope.launch {
            try {
                if (isInternetAvailable(context)) {
                    val places = repository.getPlaces(locationString, radius, type)
                    _supermarkets.value = places
                } else {
                    Log.e("MapScreenViewModel", "No hay conexión a internet.")
                }
            } catch (e: Exception) {
                Log.e("MapScreenViewModel", "Error al obtener supermercados: ${e.message}")
            }
        }
    }
}

/*class MapScreenViewModel(
    application: Application,
) : AndroidViewModel(application) {
    private val _userLocation = MutableStateFlow<Location?>(null)
    val userLocation: StateFlow<Location?> = _userLocation

    private val _supermarkets = MutableStateFlow<List<PlaceResult>>(emptyList())
    val supermarkets: StateFlow<List<PlaceResult>> = _supermarkets

    private val repository = PlacesRepository(ApiPlacesGoogleService.apiService)
    private val apiKey = "AIzaSyBfRgxJzA4nUvkAMPht4mVjkg23RPS1joI"

    init {
        getUserLocation()
    }

    @SuppressLint("MissingPermission")
    fun getUserLocation() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplication())
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            _userLocation.value = location
            fetchSupermarkets(location)
        }
    }

    private fun fetchSupermarkets(location: Location) {
        val locationString = "${location.latitude},${location.longitude}"
        val radius = 1000 // 1 km
        val type = "supermarket"

        viewModelScope.launch {
            repository.getNearbyPlaces(locationString, radius, type, apiKey) { places, error ->
                if (places != null) {
                    _supermarkets.value = places
                }
            }
        }
    }
}


class MapScreenViewModel(
    application: Application,
) : AndroidViewModel(application) {
    private val _locationState = MutableStateFlow<LatLng?>(null)
    val locationState: StateFlow<LatLng?> = _locationState

    private val _permissionsGranted = MutableStateFlow(false)
    val permissionsGranted: StateFlow<Boolean> = _permissionsGranted

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)

    fun updatePermissionsStatus(granted: Boolean) {
        _permissionsGranted.value = granted
        if (granted) fetchCurrentLocation()
    }

    @SuppressLint("MissingPermission")
    private fun fetchCurrentLocation() {
        viewModelScope.launch {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    _locationState.value = LatLng(it.latitude, it.longitude)
                }
            }
        }
    }
}
*/
