package ar.edu.unlam.mobile.scaffolding.domain.shoppinglist

import ar.edu.unlam.mobile.scaffolding.data.local.shoppinglist.ItemWithQuantityAndChecked
import ar.edu.unlam.mobile.scaffolding.data.local.shoppinglist.ShoppingListWithItems
import kotlinx.coroutines.flow.Flow

interface ShoppingListsUseCases {
    /**
     * Obtener todas las listas de compras
     */

    fun getAllShoppingLists(): Flow<List<ShoppingListModel>>

    /**
     * Obtener una lista de compras específica
     */
    fun getShoppingList(id: Long): Flow<ShoppingListModel?>

    /**
     * Insertar una lista de compras
     */
    suspend fun insertShoppingList(shoppingList: ShoppingListModel)

    /**
     * Eliminar una lista de compras
     */
    suspend fun deleteShoppingList(shoppingList: ShoppingListModel)

    /**
     * Actualizar una lista de compras
     */
    suspend fun updateShoppingList(shoppingList: ShoppingListModel)

    /**
     * Añadir un item a una lista de compras
     */
    suspend fun addItemToList(
        listId: Long,
        itemId: Long,
        quantity: Int,
        isChecked: Boolean,
    )

    /**
     * Eliminar un item de una lista
     */
    suspend fun deleteItemFromList(
        listId: Long,
        itemId: Long,
    )

    /**
     * Obtener una lista de compras con sus ítems
     */
    fun getShoppingListWithItems(listId: Long): Flow<ShoppingListWithItems>

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
