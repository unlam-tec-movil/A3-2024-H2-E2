package ar.edu.unlam.mobile.scaffolding.ui.screens
import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ar.edu.unlam.mobile.scaffolding.R
import ar.edu.unlam.mobile.scaffolding.ShopListTopAppBar
import ar.edu.unlam.mobile.scaffolding.ui.components.UbicacionHandler
import ar.edu.unlam.mobile.scaffolding.ui.navigation.NavigationDestination
import ar.edu.unlam.mobile.scaffolding.ui.screens.MapScreenDestination.titleRes
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

object MapScreenDestination : NavigationDestination {
    override val route = "map"
    override val titleRes = R.string.supermercados_cercanos
}

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

    var showPermissionDeniedMessage by remember { mutableStateOf(false) }

    MaterialTheme(colorScheme = lightColorScheme()) {
        Scaffold(
            topBar = {
                ShopListTopAppBar(
                    title = stringResource(titleRes),
                    canNavigateBack = true,
                    navigateUp = { navController.navigateUp() },
                )
            },
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                // Usar el PermissionHandler
                UbicacionHandler(
                    permission = Manifest.permission.ACCESS_FINE_LOCATION,
                    rationaleMessage = "Para mostrar el mapa y los supermercados cercanos, necesitamos acceso a tu ubicación.",
                    onPermissionGranted = {
                        viewModel.getUserLocation()
                    },
                    onPermissionDenied = {
                        showPermissionDeniedMessage = true
                    },
                )

                if (showPermissionDeniedMessage) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = "El permiso de ubicación es necesario para usar esta función.",
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            color = Color.Red,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = {
                            showPermissionDeniedMessage = false
                            navController.popBackStack()
                        }) {
                            Text("Aceptar")
                        }
                    }
                } else if (userLocation != null) {
                    // Mostrar el mapa si se obtuvo la ubicación
                    Supermekado(userLocation, supermarkets)
                }
            }
        }
    }
}

@Composable
fun Supermekado(
    userLocation: Location?,
    supermarkets: List<PlaceResult>,
    modifier: Modifier = Modifier,
) {
    val radiusInMeters = 1000f // 1km
    // Estado de la cámara del mapa
    val cameraPositionState = rememberCameraPositionState()

    // LaunchedEffect para cambiar la posición de la cámara cuando se obtenga la ubicación del usuario
    LaunchedEffect(userLocation) {
        userLocation?.let {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(it.latitude, it.longitude),
                    14f, // Nivel de zoom inicial
                ),
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

        val bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)
        // Iterar sobre la lista de supermercados y añadir un marcador para cada uno
        supermarkets.forEach { place ->
            Marker(
                state = rememberMarkerState(position = LatLng(place.geometry.location.lat, place.geometry.location.lng)),
                title = place.name, // Nombre del supermercado
                snippet = place.vicinity, // Dirección del supermercado
                icon = bitmapDescriptor, // Ícono personalizado
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
