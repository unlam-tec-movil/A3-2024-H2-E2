package ar.edu.unlam.mobile.scaffolding.ui.navigation

import ar.edu.unlam.mobile.scaffolding.R

sealed class AppScreens(
    val route: String,
    val titleRes: Int? = null,
) {
    data object Home : AppScreens("home", titleRes = R.string.title_home)

    data object NewList : AppScreens("newList")

    data object ShoppingList : AppScreens("shoppingList")

    data object AddItemsToList : AppScreens("addItemsToList")
}
