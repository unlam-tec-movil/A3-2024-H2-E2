package ar.edu.unlam.mobile.scaffolding.ui.screens

import android.annotation.SuppressLint
import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
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

/*
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
