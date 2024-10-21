package ar.edu.unlam.mobile.scaffolding.domain.item.services

import ar.edu.unlam.mobile.scaffolding.domain.item.repository.ItemRepository
import ar.edu.unlam.mobile.scaffolding.domain.item.usecases.ItemsUseCases
import ar.edu.unlam.mobile.scaffolding.domain.shoppinglist.models.ItemModel
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
