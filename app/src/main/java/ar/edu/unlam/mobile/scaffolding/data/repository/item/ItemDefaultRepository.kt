package ar.edu.unlam.mobile.scaffolding.data.repository.item

import ar.edu.unlam.mobile.scaffolding.domain.item.ItemModel
import ar.edu.unlam.mobile.scaffolding.domain.item.ItemRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ItemDefaultRepository
    @Inject
    constructor(
        private val local: ItemLocalRepository,
    ) : ItemRepository {
        override fun getAllItemsStream(): Flow<List<ItemModel>> = local.getAllItemsStream()

        override fun getItemStream(id: Int): Flow<ItemModel> = local.getItemStream(id)

        override suspend fun insertItem(item: ItemModel) = local.insertItem(item)

        override suspend fun deleteItem(item: ItemModel) = local.deleteItem(item)

        override suspend fun updateItem(item: ItemModel) = local.deleteItem(item)
    }
