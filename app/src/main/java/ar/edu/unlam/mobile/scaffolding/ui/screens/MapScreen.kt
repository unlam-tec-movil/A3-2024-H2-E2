

package ar.edu.unlam.mobile.scaffolding.ui.screens

import android.Manifest
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ar.edu.unlam.mobile.scaffolding.R
import ar.edu.unlam.mobile.scaffolding.ui.navigation.NavigationDestination
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

object Mapa : NavigationDestination {
    override val route = "mapa"
    override val titleRes = R.string.mis_listas
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    locationViewModel: MapScreenViewModel = viewModel(),
    navController: NavController,
) {
    val location by locationViewModel.locationState.collectAsState()
    val permissionsGranted by locationViewModel.permissionsGranted.collectAsState()

    // Manejo de permisos
    val context = LocalContext.current
    val permissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    LaunchedEffect(permissionState.status.isGranted) {
        locationViewModel.updatePermissionsStatus(permissionState.status.isGranted)
    }

    if (!permissionsGranted) {
        RequestLocationPermissionDialog(
            permissionState = permissionState,
            onPermissionRequest = { permissionState.launchPermissionRequest() },
        )
    } else {
        // Renderizamos el mapa
        SupermercadosMap(location)
    }
}

@Composable
fun SupermercadosMap(userLocation: LatLng?) {
    val radiusInMeters = 1000f
    val mapState =
        rememberCameraPositionState {
            this.position = CameraPosition.fromLatLngZoom(userLocation ?: LatLng(0.0, 0.0), 4f)
        }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = mapState,
    ) {
        userLocation?.let {
            val markerState = remember { MarkerState(position = userLocation) }
            Marker(
                state = markerState,
                title = "Tu ubicación",
            )

            // Dibuja un círculo alrededor de la ubicación
            Circle(
                center = userLocation,
                radius = radiusInMeters.toDouble(),
                fillColor = Color(0x550000FF), // Color azul con opacidad
                strokeColor = Color(0xFF0000FF), // Borde azul
                strokeWidth = 2f,
            )
        }
    }
}
