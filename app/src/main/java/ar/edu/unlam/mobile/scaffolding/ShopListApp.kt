package ar.edu.unlam.mobile.scaffolding

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ar.edu.unlam.mobile.scaffolding.ui.components.BottomBar
import ar.edu.unlam.mobile.scaffolding.ui.navigation.AppNavHost
import ar.edu.unlam.mobile.scaffolding.ui.navigation.AppScreens
import ar.edu.unlam.mobile.scaffolding.ui.screens.AdditemsDestination
import ar.edu.unlam.mobile.scaffolding.ui.screens.HomeDestination
import ar.edu.unlam.mobile.scaffolding.ui.screens.NewListDestination
import ar.edu.unlam.mobile.scaffolding.ui.screens.ShoppingListDestination
import kotlinx.coroutines.launch

@Composable
fun ShopListApp2(navController: NavHostController = rememberNavController()) {
    AppNavHost(navController = navController)
}

/**
 * App bar to display title and conditionally display the back navigation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopListTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateUp: () -> Unit = {},
    onOpenDrawer: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFFFFA500),
                titleContentColor = Color.White,
                actionIconContentColor = Color.White,
            ),
        /*  actions = {
              IconButton(onClick = { }) {
                  Icon(Icons.Default.Settings, contentDescription = "Settings")
              }
          },*/
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button),
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = null,
                    modifier =
                        Modifier.clickable {
                            onOpenDrawer()
                        },
                )
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopListApp() {
    val controller = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    val menuItems = listOf("Mapa", "Listas Archivadas", "Comparador")

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
                                "Mapa" -> controller.navigate(AppScreens.Places.route)
                                "Listas Archivadas" -> controller.navigate("settings")
                                "Comparador" -> { // Acción de cerrar sesión
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
                        HomeDestination.route -> stringResource(HomeDestination.titleRes)
                        NewListDestination.route -> stringResource(NewListDestination.titleRes)
                        ShoppingListDestination.routeWithArgs -> "Compras app"
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
                    HomeDestination.route -> {
                        AddFAB(navController = controller, "newList")
                    }

                    NewListDestination.route -> {}
                    ShoppingListDestination.routeWithArgs -> {
                        val listId =
                            controller.currentBackStackEntry
                                ?.savedStateHandle
                                ?.get<Long>(ShoppingListDestination.LIST_ID_ARG)
                        Log.d("ListId", "listId en floating action button: $listId")
                        if (listId != null) {
                            AddFAB(
                                navController = controller,
                                route = AdditemsDestination.route,
                                listId = listId,
                            )
                        }
                    }
                }
            },
//            floatingActionButton = {
//                IconButton(onClick = { controller.navigate("home") }) {
//                    Icon(Icons.Filled.Home, contentDescription = "Home")
//                }
//            },
        ) { paddingValue ->
            AppNavHost(navController = controller, modifier = Modifier.padding(paddingValue))
        }
    }
}

@Composable
private fun AddFAB(
    navController: NavHostController,
    route: String,
    listId: Long? = null,
) {
    FloatingActionButton(onClick = {
        val finalRoute = listId?.let { "$route/$it" } ?: route
        navController.navigate(finalRoute)
    }) {
        Icon(Icons.Filled.Add, contentDescription = "Add items to list")
    }
}
