package ar.edu.unlam.mobile.scaffolding.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ar.edu.unlam.mobile.scaffolding.ui.screens.AddItemsToShoppingListScreen
import ar.edu.unlam.mobile.scaffolding.ui.screens.AdditemsDestination
import ar.edu.unlam.mobile.scaffolding.ui.screens.HomeDestination
import ar.edu.unlam.mobile.scaffolding.ui.screens.HomeScreen
import ar.edu.unlam.mobile.scaffolding.ui.screens.MapScreen
import ar.edu.unlam.mobile.scaffolding.ui.screens.MapScreenDestination
import ar.edu.unlam.mobile.scaffolding.ui.screens.NewListDestination
import ar.edu.unlam.mobile.scaffolding.ui.screens.NewListScreen
import ar.edu.unlam.mobile.scaffolding.ui.screens.ShoppingListDestination
import ar.edu.unlam.mobile.scaffolding.ui.screens.ShoppingListScreen
import ar.edu.unlam.mobile.scaffolding.ui.viewmodels.HomeViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val viewModel: HomeViewModel = hiltViewModel()

    // NavHost es el componente que funciona como contenedor de los otros componentes que
    // podrán ser destinos de navegación.
    NavHost(navController = navController, startDestination = "home") {
        // composable es el componente que se usa para definir un destino de navegación.
        // Por parámetro recibe la ruta que se utilizará para navegar a dicho destino.
        composable(route = HomeDestination.route) {
            // Home es el componente en sí que es el destino de navegación.
            HomeScreen(
                modifier = modifier,
                navigateToList = { navController.navigate(route = "${ShoppingListDestination.route}/$it") },
                navigateToNewList = { navController.navigate(route = NewListDestination.route) },
                navController = navController,
            )
        }
        composable(NewListDestination.route) {
            NewListScreen(
                modifier = modifier,
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
            )
        }
        composable(
            route = ShoppingListDestination.routeWithArgs,
            arguments =
                listOf(
                    navArgument(ShoppingListDestination.LIST_ID_ARG) {
                        type = NavType.LongType
                    },
                ),
        ) {
            ShoppingListScreen(
                navigateToAddItem = { listId ->
                    navController.navigate(
                        route = "${AdditemsDestination.route}/$listId",
                    )
                },
                navController = navController,
                modifier = modifier,
            )
        }
        composable(
            route = AdditemsDestination.routeWithArgs,
            arguments =
                listOf(
                    navArgument(AdditemsDestination.LIST_ID_ARG) {
                        type = NavType.LongType
                    },
                ),
        ) {
            AddItemsToShoppingListScreen(
                navController = navController,
                modifier = modifier,
            )
        }

        // para ir a la pantalla de mapa
        composable(route = MapScreenDestination.route) {
            MapScreen(navController = navController)
        }
    }
}
