package ar.edu.unlam.mobile.scaffolding.domain.shoppinglist.usecases

import ar.edu.unlam.mobile.scaffolding.domain.shoppinglist.models.ShoppingListModel
import kotlinx.coroutines.flow.Flow

interface ShoppingListsUseCases {
    //
    suspend fun getAllShoppingLists(): Flow<List<ShoppingListModel>>

    //
    suspend fun getShoppingList(id: Int): Flow<ShoppingListModel?>

    //
    suspend fun insertShoppingList(shoppingList: ShoppingListModel)

    //
    suspend fun deleteShoppingList(shoppingList: ShoppingListModel)

    //
    suspend fun updateShoppingList(shoppingList: ShoppingListModel)
}
