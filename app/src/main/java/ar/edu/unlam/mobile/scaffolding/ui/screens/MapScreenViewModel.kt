package ar.edu.unlam.mobile.scaffolding.ui.screens

import android.annotation.SuppressLint
import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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
