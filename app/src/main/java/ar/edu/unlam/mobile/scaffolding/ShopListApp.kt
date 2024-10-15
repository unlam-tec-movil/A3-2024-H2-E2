package ar.edu.unlam.mobile.scaffolding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ar.edu.unlam.mobile.scaffolding.ui.components.BottomBar
import ar.edu.unlam.mobile.scaffolding.ui.navigation.AppNavHost
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopListApp() {
    val controller = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    val menuItems = listOf("Home", "Perfil", "Configuración", "Cerrar sesión")

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            // Contenido del menú hamburguesa
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.primaryContainer),
            ) {
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
                                "Cerrar sesión" -> { // Acción de cerrar sesión
                                }
                            }
                        },
                    ) {
                        Text(text = item, modifier = Modifier.padding(16.dp))
                    }
                }
            }
        },
    ) {
        val currentDestination =
            controller
                .currentBackStackEntryAsState()
                .value
                ?.destination
                ?.route
        Scaffold(
            topBar = {
                val currentDestination =
                    controller
                        .currentBackStackEntryAsState()
                        .value
                        ?.destination
                        ?.route
                val title =
                    when (currentDestination) {
                        "home" -> "Mis listas"
                        "newList" -> "Nueva Lista"
                        else -> "ComprasApp"
                    }

                TopAppBar(
                    title = { Text(title) },
                    colors =
                        TopAppBarDefaults.topAppBarColors(
                            containerColor = Color(0xFFFFA500),
                            titleContentColor = Color.White,
                            actionIconContentColor = Color.White,
                        ),
                    actions = {
                        IconButton(onClick = { }) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings")
                        }
                    },
                )
            },
            bottomBar = { BottomBar(controller = controller) },
            floatingActionButton = {
                when (currentDestination) {
                    "home" -> {
                        AddFAB(navController = controller, "newList")
                    }

                    "newList" -> {}
                    "shoppingList" -> {
                        AddFAB(navController = controller, "addItemsToList")
                    }
                }
            },
//            floatingActionButton = {
//                IconButton(onClick = { controller.navigate("home") }) {
//                    Icon(Icons.Filled.Home, contentDescription = "Home")
//                }
//            },
        ) { paddingValue ->
            AppNavHost(controller = controller, modifier = Modifier.padding(paddingValue))
        }
    }
}

@Composable
private fun AddFAB(
    navController: NavHostController,
    route: String,
) {
    FloatingActionButton(onClick = {
        navController.navigate(route)
    }) {
        Icon(Icons.Filled.Add, contentDescription = "Add items to list")
    }
}
