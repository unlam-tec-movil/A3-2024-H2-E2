package ar.edu.unlam.mobile.scaffolding.ui.navigation

import ar.edu.unlam.mobile.scaffolding.R

sealed class AppScreens(
    val route: String,
    val titleRes: Int? = null,
) {
    object Home : AppScreens("home", titleRes = R.string.title_home)

    object NewList : AppScreens("newList")

    object ShoppingList : AppScreens("shoppingList")

    object AddItemsToList : AppScreens("addItemsToList")
}
