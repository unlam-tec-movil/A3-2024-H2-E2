package ar.edu.unlam.mobile.scaffolding.data.repository.shoppinglist

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

        override fun getShoppingListStream(id: Int): Flow<ShoppingListModel> = local.getShoppingListStream(id)

        override suspend fun insertShoppingList(shoppingList: ShoppingListModel) = local.insertShoppingList(shoppingList)

        override suspend fun deleteShoppingList(shoppingList: ShoppingListModel) = local.deleteShoppingList(shoppingList)

        override suspend fun updateShoppingList(shoppingList: ShoppingListModel) = local.updateShoppingList(shoppingList)
    }
