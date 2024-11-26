package ar.edu.unlam.mobile.scaffolding.ui.screens
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
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
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ar.edu.unlam.mobile.scaffolding.R
import ar.edu.unlam.mobile.scaffolding.ShopListTopAppBar
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
        var showDialog by remember { mutableStateOf(!hasPermission) }
        var showOptionalMessage by remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                ShopListTopAppBar(
                    title = stringResource(titleRes),
                    canNavigateBack = true,
                    navigateUp = { navController.navigateUp() },
                )
            },
        ) { paddingValues ->
            if (hasPermission) {
                // Mostrar el mapa si se tiene permiso
                Supermekado(userLocation, supermarkets)
            } else {
                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { /* No hacer nada para evitar que se cierre accidentalmente */ },
                        title = { Text(text = "Permiso de Ubicación Necesario") },
                        text = {
                            Text(
                                text =
                                    "Para mostrar el mapa y los supermercados cercanos, se necesita acceder a tu ubicación. " +
                                        "Por favor, concede el permiso para continuar.",
                            )
                        },
                        confirmButton = {
                            Button(onClick = {
                                showDialog = false
                                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                            }) {
                                Text("Conceder Permiso")
                            }
                        },
                        dismissButton = {
                            Button(onClick = {
                                showDialog = false
                                showOptionalMessage = true
                            }) {
                                Text("Volver")
                            }
                        },
                    )
                } else if (showOptionalMessage) {
                    // Mensaje opcional con texto grande y navegación automática
                    LaunchedEffect(Unit) {
                        kotlinx.coroutines.delay(1500)
                        navController.navigateUp()
                    }
                    Column(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                                .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = "El permiso de ubicación es necesario para usar esta función.",
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                        )
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
