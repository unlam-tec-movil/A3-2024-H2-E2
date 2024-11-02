package ar.edu.unlam.mobile.scaffolding.data.repository.shoppinglist

import ar.edu.unlam.mobile.scaffolding.data.local.shoppinglist.ItemWithQuantityAndChecked
import ar.edu.unlam.mobile.scaffolding.data.local.shoppinglist.ShoppingListWithItems
import ar.edu.unlam.mobile.scaffolding.domain.shoppinglist.ShoppingListModel
import ar.edu.unlam.mobile.scaffolding.domain.shoppinglist.ShoppingListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShoppingListDefaultRepository
    @Inject
    constructor(
        private val local: ShoppingListLocalRepository,
    ) : ShoppingListRepository {
        override fun getAllShoppingListsStream(): Flow<List<ShoppingListModel>> = local.getAllShoppingListsStream()

        override fun getShoppingListStream(id: Long): Flow<ShoppingListModel> = local.getShoppingListStream(id)

        override suspend fun insertShoppingList(shoppingList: ShoppingListModel) = local.insertShoppingList(shoppingList)

        override suspend fun deleteShoppingList(shoppingList: ShoppingListModel) = local.deleteShoppingList(shoppingList)

        override suspend fun updateShoppingList(shoppingList: ShoppingListModel) = local.updateShoppingList(shoppingList)

        override suspend fun addItemToList(
            listId: Long,
            itemId: Long,
            quantity: Int,
            isChecked: Boolean,
        ) {
            local.addItemToList(listId, itemId, quantity, isChecked)
        }

        override suspend fun deleteItemFromList(
            listId: Long,
            itemId: Long,
        ) {
            local.deleteItemFromList(listId, itemId)
        }

        override fun getShoppingListWithItemsStream(listId: Long): Flow<ShoppingListWithItems> =
            local.getShoppingListWithItemsStream(listId)

        override fun getItemsForShoppingList(listId: Long): Flow<List<ItemWithQuantityAndChecked>> = local.getItemsForShoppingList(listId)

        override suspend fun updateItemCheckedState(
            itemId: Long,
            listId: Long,
            checked: Boolean,
        ) {
            local.updateItemCheckedState(itemId, listId, checked)
        }
    }
