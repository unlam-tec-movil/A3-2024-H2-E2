package ar.edu.unlam.mobile.scaffolding.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ar.edu.unlam.mobile.scaffolding.ui.screens.AddItemsToShoppingListScreen
import ar.edu.unlam.mobile.scaffolding.ui.screens.HomeScreen
import ar.edu.unlam.mobile.scaffolding.ui.screens.NewListScreen
import ar.edu.unlam.mobile.scaffolding.ui.screens.ShoppingListScreen

@Composable
fun AppNavHost(
    controller: NavHostController,
    modifier: Modifier = Modifier,
) {
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
            NewListScreen(modifier = modifier)
        }
        composable(AppScreens.ShoppingList.route) {
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
