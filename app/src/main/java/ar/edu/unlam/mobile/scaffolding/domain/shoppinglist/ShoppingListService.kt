package ar.edu.unlam.mobile.scaffolding.domain.shoppinglist

import ar.edu.unlam.mobile.scaffolding.data.local.shoppinglist.ItemWithQuantityAndChecked
import ar.edu.unlam.mobile.scaffolding.data.local.shoppinglist.ShoppingListWithItems
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShoppingListService
    @Inject
    constructor(
        private val shoppingListRepository: ShoppingListRepository,
    ) : ShoppingListsUseCases {
        override fun getAllShoppingLists(): Flow<List<ShoppingListModel>> = shoppingListRepository.getAllShoppingListsStream()

        override fun getShoppingList(id: Long): Flow<ShoppingListModel> = shoppingListRepository.getShoppingListStream(id)

        override suspend fun insertShoppingList(shoppingList: ShoppingListModel) = shoppingListRepository.insertShoppingList(shoppingList)

        override suspend fun deleteShoppingList(shoppingList: ShoppingListModel) = shoppingListRepository.deleteShoppingList(shoppingList)

        override suspend fun updateShoppingList(shoppingList: ShoppingListModel) = shoppingListRepository.updateShoppingList(shoppingList)

        override suspend fun addItemToList(
            listId: Long,
            itemId: Long,
            quantity: Int,
            isChecked: Boolean,
        ) {
            shoppingListRepository.addItemToList(listId, itemId, quantity, isChecked)
        }

        override suspend fun deleteItemFromList(
            listId: Long,
            itemId: Long,
        ) {
            shoppingListRepository.deleteItemFromList(listId, itemId)
        }

        override fun getShoppingListWithItems(listId: Long): Flow<ShoppingListWithItems> =
            shoppingListRepository.getShoppingListWithItemsStream(listId)

        override fun getItemsForShoppingList(listId: Long): Flow<List<ItemWithQuantityAndChecked>> =
            shoppingListRepository.getItemsForShoppingList(listId)

        override suspend fun updateItemCheckedState(
            itemId: Long,
            listId: Long,
            checked: Boolean,
        ) {
            shoppingListRepository.updateItemCheckedState(itemId, listId, checked)
        }
    }
