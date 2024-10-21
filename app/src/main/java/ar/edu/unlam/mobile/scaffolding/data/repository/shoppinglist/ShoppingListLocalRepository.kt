package ar.edu.unlam.mobile.scaffolding.data.repository.shoppinglist

import ar.edu.unlam.mobile.scaffolding.domain.shoppinglist.ShoppingListModel
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio de listas de compras.
 */
interface ShoppingListLocalRepository {
    /**
     * Obtiene todas las listas de compras.
     */
    fun getAllShoppingListsStream(): Flow<List<ShoppingListModel>>

    /**
     * Obtiene una lista de compras por su ID.
     */
    fun getShoppingListStream(id: Int): Flow<ShoppingListModel>

    /**
     * Inserta una nueva lista de compras.
     */
    suspend fun insertShoppingList(shoppingList: ShoppingListModel)

    /**
     * Elimina una lista de compras.
     */
    suspend fun deleteShoppingList(shoppingList: ShoppingListModel)

    /**
     * Actualiza una lista de compras.
     */
    suspend fun updateShoppingList(shoppingList: ShoppingListModel)
}
