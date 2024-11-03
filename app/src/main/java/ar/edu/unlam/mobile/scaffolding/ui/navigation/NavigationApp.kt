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
import ar.edu.unlam.mobile.scaffolding.ui.screens.HomeScreen
import ar.edu.unlam.mobile.scaffolding.ui.screens.NewListScreen
import ar.edu.unlam.mobile.scaffolding.ui.screens.ShoppingListScreen
import ar.edu.unlam.mobile.scaffolding.ui.viewmodels.HomeViewModel

@Composable
fun AppNavHost(
    controller: NavHostController,
    modifier: Modifier = Modifier,
) {
    val viewModel: HomeViewModel = hiltViewModel()

    // NavHost es el componente que funciona como contenedor de los otros componentes que
    // podrán ser destinos de navegación.
    NavHost(navController = controller, startDestination = "home") {
        // composable es el componente que se usa para definir un destino de navegación.
        // Por parámetro recibe la ruta que se utilizará para navegar a dicho destino.
        composable(AppScreens.Home.route) {
            // Home es el componente en sí que es el destino de navegación.
            HomeScreen(
                modifier = modifier,
                navController = controller,
            )
        }
        composable(AppScreens.NewList.route) {
            NewListScreen(modifier = modifier, navController = controller)
        }
        composable(
            route = "${AppScreens.ShoppingList.route}/{listId}",
            arguments = listOf(navArgument("listId") { type = NavType.LongType }),
        ) {
            ShoppingListScreen(
                modifier = modifier,
                navController = controller,
            )
        }
        composable(AppScreens.AddItemsToList.route) {
            AddItemsToShoppingListScreen(modifier = modifier)
        }
    }
}
