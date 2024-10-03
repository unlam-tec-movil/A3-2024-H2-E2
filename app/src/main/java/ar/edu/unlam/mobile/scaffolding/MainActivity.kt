package ar.edu.unlam.mobile.scaffolding

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ar.edu.unlam.mobile.scaffolding.ui.components.BottomBar
import ar.edu.unlam.mobile.scaffolding.ui.screens.HomeScreen
import com.example.compose.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    // Controller es el elemento que nos permite navegar entre pantallas. Tiene las acciones
    // para navegar como naviegate y también la información de en dónde se "encuentra" el usuario
    // a través del back stack
    val controller = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    val menuItems = listOf("Home", "Perfil", "Configuración", "Cerrar sesión")

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            // Contenido del menú hamburguesa
            Column(modifier = Modifier.fillMaxSize().background(Color(0xFFFFA726))) {
                Text(
                    text = "Menú",
                    modifier = Modifier.padding(16.dp),
                )
                Divider()
                menuItems.forEach { item ->
                    TextButton(
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            // Aquí puedes agregar acciones según el item seleccionado
                            when (item) {
                                "Home" -> controller.navigate("home")
                                "Perfil" -> controller.navigate("profile")
                                "Configuración" -> controller.navigate("settings")
                                "Cerrar sesión" -> {/* Acción de cerrar sesión */
                                }
                            }
                        }
                    ) {
                        Text(text = item, modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Nombre App") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFFFFA500),
                        titleContentColor = Color.White,
                        actionIconContentColor = Color.White
                    ),
                    actions = {
                        IconButton(onClick = { }) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings")
                        }
                    }
                )
            },
            bottomBar = { BottomBar(controller = controller) },
            floatingActionButton = {
                IconButton(onClick = { controller.navigate("home") }) {
                    Icon(Icons.Filled.Home, contentDescription = "Home")
                }
            },
        ) { paddingValue ->
            // NavHost es el componente que funciona como contenedor de los otros componentes que
            // podrán ser destinos de navegación.
            NavHost(navController = controller, startDestination = "home") {
                // composable es el componente que se usa para definir un destino de navegación.
                // Por parámetro recibe la ruta que se utilizará para navegar a dicho destino.
                composable("home") {
                    // Home es el componente en sí que es el destino de navegación.
                    HomeScreen(modifier = Modifier.padding(paddingValue))
                }
            }
        }
    }
}
