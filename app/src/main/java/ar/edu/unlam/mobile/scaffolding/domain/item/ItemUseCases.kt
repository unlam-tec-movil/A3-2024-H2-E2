package ar.edu.unlam.mobile.scaffolding.domain.item

import kotlinx.coroutines.flow.Flow

interface ItemsUseCases {
    suspend fun getAllItemsStream(): Flow<List<ItemModel>>

    suspend fun getItemStream(id: Int): Flow<ItemModel>

    suspend fun insertItem(item: ItemModel)

    suspend fun deleteItem(item: ItemModel)

    suspend fun updateItem(item: ItemModel)
}
