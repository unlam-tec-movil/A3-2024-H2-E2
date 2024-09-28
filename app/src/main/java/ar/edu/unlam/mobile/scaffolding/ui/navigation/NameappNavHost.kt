package ar.edu.unlam.mobile.scaffolding.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ar.edu.unlam.mobile.scaffolding.ui.screens.AddItemsToShoppingListDestination
import ar.edu.unlam.mobile.scaffolding.ui.screens.AddItemsToShoppingListScreen
import ar.edu.unlam.mobile.scaffolding.ui.screens.HomeDestination
import ar.edu.unlam.mobile.scaffolding.ui.screens.HomeScreen
import ar.edu.unlam.mobile.scaffolding.ui.screens.ShoppingListDestination
import ar.edu.unlam.mobile.scaffolding.ui.screens.ShoppingListScreen

@Composable
fun NameappNavHost(navController: NavHostController,modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToAddShoppingList = { navController.navigate(ShoppingListDestination.route) },
                navigateToItemUpdate = { /*TODO navegar a la pantalla con la lista de compras*/ }
            )

        }
        composable(route = ShoppingListDestination.route) {
            ShoppingListScreen(
                navigateBack = { navController.navigateUp() },
                onNavigateUp = { navController.navigateUp() },
                navigateToAddItems = { navController.navigate(AddItemsToShoppingListDestination.route) }
            )
        }
        composable(route = AddItemsToShoppingListDestination.route) {
            AddItemsToShoppingListScreen(
                navigateBack = { navController.navigateUp() },
                navigateUp = { navController.navigateUp() }
            )
        }
    }
}
