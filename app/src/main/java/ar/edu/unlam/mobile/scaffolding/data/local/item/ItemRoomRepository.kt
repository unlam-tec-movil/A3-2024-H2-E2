package ar.edu.unlam.mobile.scaffolding.data.local.item

import ar.edu.unlam.mobile.scaffolding.data.local.AppDatabase
import ar.edu.unlam.mobile.scaffolding.data.repository.item.ItemLocalRepository
import ar.edu.unlam.mobile.scaffolding.domain.shoppinglist.models.ItemModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ItemRoomRepository
    @Inject
    constructor(
        appDatabase: AppDatabase,
    ) : ItemLocalRepository {
        private val itemDao = appDatabase.itemDao()

        override fun getAllItemsStream(): Flow<List<ItemModel>> =
            itemDao.getAllItems().map {
                it.map { itemEntity ->
                    itemEntity.asModel()
                }
            }

        override fun getItemStream(id: Int): Flow<ItemModel> = itemDao.getItemById(id).map { it.asModel() }

        override suspend fun insertItem(item: ItemModel) = itemDao.insert(item.asEntity())

        override suspend fun deleteItem(item: ItemModel) = itemDao.delete(item.asEntity())

        override suspend fun updateItem(item: ItemModel) = itemDao.update(item.asEntity())
    }
