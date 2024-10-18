package ar.edu.unlam.mobile.scaffolding.data.repository

import ar.edu.unlam.mobile.scaffolding.data.local.ShoppingList
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio de listas de compras.
 */
interface ShoppingListsRepository {
    /**
     * Obtiene todas las listas de compras.
     */
    fun getAllShoppingListsStream(): Flow<List<ShoppingList>>

    /**
     * Obtiene una lista de compras por su ID.
     */
    fun getShoppingListStream(id: Int): Flow<ShoppingList?>

    /**
     * Inserta una nueva lista de compras.
     */
    suspend fun insertShoppingList(shoppingList: ShoppingList)

    /**
     * Elimina una lista de compras.
     */
    suspend fun deleteShoppingList(shoppingList: ShoppingList)

    /**
     * Actualiza una lista de compras.
     */
    suspend fun updateShoppingList(shoppingList: ShoppingList)
}
