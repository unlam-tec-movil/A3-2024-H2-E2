package ar.edu.unlam.mobile.scaffolding.ui.screens

import android.annotation.SuppressLint
import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unlam.mobile.scaffolding.data.local.AppDatabase
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// esto es una prueba
class MapScreenViewModel(
    application: Application,
) : AndroidViewModel(application) {
    private val _userLocation = MutableStateFlow<Location?>(null)
    val userLocation: StateFlow<Location?> = _userLocation

    private val _supermarkets = MutableStateFlow<List<PlaceResult>>(emptyList())
    val supermarkets: StateFlow<List<PlaceResult>> = _supermarkets

    private val repository =
        PlacesRepository(
            ApiPlacesGoogleService.apiService,
            AppDatabase.getDatabase(application).placeDao(),
            AppDatabase.getDatabase(application).locationDao(),
        )
    private val apiKey = "AIzaSyBfRgxJzA4nUvkAMPht4mVjkg23RPS1joI"

    init {
        getUserLocation()
    }

    @SuppressLint("MissingPermission")
    fun getUserLocation() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplication())
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                // Guardar la ubicación de android.location.Location en la base de datos
                viewModelScope.launch {
                    repository.saveUserLocation(it) // Usando android.location.Location directamente
                    _userLocation.value = it
                    fetchSupermarkets(it)
                }
            } ?: run {
                // Si no se obtiene la ubicación, intentar obtenerla de la base de datos
                viewModelScope.launch {
                    repository.getUserLocation { savedLocation ->
                        savedLocation?.let {
                            _userLocation.value = it
                            fetchSupermarkets(it)
                        }
                    }
                }
            }
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
