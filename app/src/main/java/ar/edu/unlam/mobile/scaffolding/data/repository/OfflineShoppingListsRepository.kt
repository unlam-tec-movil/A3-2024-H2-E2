package ar.edu.unlam.mobile.scaffolding.data.repository

import ar.edu.unlam.mobile.scaffolding.data.local.ShoppingList
import ar.edu.unlam.mobile.scaffolding.data.local.ShoppingListDao
import kotlinx.coroutines.flow.Flow

class OfflineShoppingListsRepository(
    private val shoppingListDao: ShoppingListDao,
) : ShoppingListsRepository {
    override fun getAllShoppingListsStream(): Flow<List<ShoppingList>> = shoppingListDao.getAllShoppingLists()

    override fun getShoppingListStream(id: Int): Flow<ShoppingList?> = shoppingListDao.getShoppingListById(id)

    override suspend fun insertShoppingList(shoppingList: ShoppingList) = shoppingListDao.insert(shoppingList)

    override suspend fun deleteShoppingList(shoppingList: ShoppingList) = shoppingListDao.delete(shoppingList)

    override suspend fun updateShoppingList(shoppingList: ShoppingList) = shoppingListDao.update(shoppingList)
}
