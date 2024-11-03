package ar.edu.unlam.mobile.scaffolding.data.repository.shoppinglist

import ar.edu.unlam.mobile.scaffolding.data.local.shoppinglist.ItemWithQuantityAndChecked
import ar.edu.unlam.mobile.scaffolding.data.local.shoppinglist.ShoppingListWithItems
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
    fun getShoppingListStream(id: Long): Flow<ShoppingListModel>

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

    /**
     * Añadir item a lista
     */
    suspend fun addItemToList(
        listId: Long,
        itemId: Long,
        quantity: Int,
        isChecked: Boolean,
    )

    /**
     * Eliminar item de lista
     */
    suspend fun deleteItemFromList(
        listId: Long,
        itemId: Long,
    )

    /**
     * Obtiene una lista de compras con sus items.
     */
    fun getShoppingListWithItemsStream(listId: Long): Flow<ShoppingListWithItems>

    /**
     * Obtiene una lista de compras con sus items.
     */
    fun getItemsForShoppingList(listId: Long): Flow<List<ItemWithQuantityAndChecked>>

    suspend fun updateItemCheckedState(
        itemId: Long,
        listId: Long,
        checked: Boolean,
    )
}
