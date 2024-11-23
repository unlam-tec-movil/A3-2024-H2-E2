package ar.edu.unlam.mobile.scaffolding.ui.screens
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    viewModel: MapScreenViewModel = viewModel(),
    navController: NavController,
) {
    val context = LocalContext.current

    // Observar la ubicación del usuario y los supermercados
    val userLocation by viewModel.userLocation.collectAsState()
    val supermarkets by viewModel.supermarkets.collectAsState()

    // Lanzador para pedir permisos
    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
        ) { isGranted ->
            if (isGranted) {
                viewModel.getUserLocation() // Si se otorga el permiso, solicita la ubicación
            }
        }

    // Comprobación inicial de permisos
    val hasPermission =
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED

    MaterialTheme(colorScheme = lightColorScheme()) {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Supermercados Cercanos") })
            },
        ) {
            if (hasPermission) {
                // Mostrar el mapa si se tiene permiso
                Supermekado(userLocation, supermarkets)
            } else {
                // Solicitar permisos si no se tienen
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text("Se necesita permiso de ubicación para mostrar el mapa")
                    Button(onClick = { permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION) }) {
                        Text("Conceder Permiso")
                    }
                }
            }
        }
    }
}

@Composable
fun Supermekado(
    userLocation: Location?,
    supermarkets: List<PlaceResult>,
) {
    val radiusInMeters = 1000f // 1km
    // Estado de la cámara del mapa
    val cameraPositionState =
        rememberCameraPositionState {
            userLocation?.let {
                position =
                    CameraPosition.fromLatLngZoom(
                        LatLng(it.latitude, it.longitude), // Verifica que it tenga estas propiedades
                        14f, // Nivel de zoom inicial
                    )
            }
        }

    // Configuración del Google Map
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
    ) {
        // Marcador para la ubicación del usuario
        userLocation?.let {
            val estado = rememberMarkerState(position = LatLng(it.latitude, it.longitude))

            Circle(
                center = LatLng(it.latitude, it.longitude),
                radius = 1000.0, // Radio en metros (1 km)
                fillColor = Color(0x5500FF00), // Color de relleno (semi-transparente verde)
                strokeColor = Color(0xFF00FF00), // Color del borde (verde)
                strokeWidth = 2f, // Ancho del borde
            )

            Marker(
                state = estado,
                title = "Tu ubicación",
                snippet = "Aquí estás",
            )
        }

        // Iterar sobre la lista de supermercados y añadir un marcador para cada uno
        supermarkets.forEach { place ->
            Marker(
                state = rememberMarkerState(position = LatLng(place.geometry.location.lat, place.geometry.location.lng)),
                title = place.name, // Nombre del supermercado
                snippet = place.vicinity, // Dirección del supermercado
                // icon = bitmapDescriptorFromVector(context, R.drawable.ic_supermarket_marker) // Ícono personalizado
            )
        }
    }
}

/*
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
*/
