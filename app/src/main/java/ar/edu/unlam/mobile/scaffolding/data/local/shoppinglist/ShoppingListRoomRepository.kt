package ar.edu.unlam.mobile.scaffolding.data.local.shoppinglist

import ar.edu.unlam.mobile.scaffolding.data.local.AppDatabase
import ar.edu.unlam.mobile.scaffolding.data.repository.shoppinglist.ShoppingListLocalRepository
import ar.edu.unlam.mobile.scaffolding.domain.shoppinglist.models.ShoppingListModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ShoppingListRoomRepository
    @Inject
    constructor(
        private val appDatabase: AppDatabase,
    ) : ShoppingListLocalRepository {
        private val shoppingListDao = appDatabase.shoppingListDao()

        override fun getAllShoppingListsStream(): Flow<List<ShoppingListModel>> =
            shoppingListDao.getAllShoppingLists().map {
                it.map { shoppingListEntity ->
                    shoppingListEntity.asModel()
                }
            }

        override fun getShoppingListStream(id: Int): Flow<ShoppingListModel> = shoppingListDao.getShoppingListById(id).map { it.asModel() }

        override suspend fun insertShoppingList(shoppingList: ShoppingListModel) {
            shoppingListDao.insert(shoppingList.asEntity())
        }

        override suspend fun deleteShoppingList(shoppingList: ShoppingListModel) {
            shoppingListDao.delete(shoppingList.asEntity())
        }

        override suspend fun updateShoppingList(shoppingList: ShoppingListModel) {
            shoppingListDao.update(shoppingList.asEntity())
        }
    }
