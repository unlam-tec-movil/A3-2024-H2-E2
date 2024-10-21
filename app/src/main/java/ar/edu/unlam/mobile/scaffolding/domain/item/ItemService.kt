package ar.edu.unlam.mobile.scaffolding.domain.item

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ItemService
    @Inject
    constructor(
        private val itemRepository: ItemRepository,
    ) : ItemsUseCases {
        override suspend fun getAllItemsStream(): Flow<List<ItemModel>> = itemRepository.getAllItemsStream()

        override suspend fun getItemStream(id: Int): Flow<ItemModel> = itemRepository.getItemStream(id)

        override suspend fun insertItem(item: ItemModel) = itemRepository.insertItem(item)

        override suspend fun deleteItem(item: ItemModel) = itemRepository.deleteItem(item)

        override suspend fun updateItem(item: ItemModel) = itemRepository.updateItem(item)
    }
