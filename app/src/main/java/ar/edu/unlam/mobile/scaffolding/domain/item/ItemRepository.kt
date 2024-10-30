package ar.edu.unlam.mobile.scaffolding.domain.item

import kotlinx.coroutines.flow.Flow

/**
 * Repositorio de listas de compras.
 */
interface ItemRepository {
    /**
     * Retrieve all the items from the the given data source.
     */
    fun getAllItemsStream(): Flow<List<ItemModel>>

    /**
     * Retrieve an item from the given data source that matches with the [id].
     */
    fun getItemStream(id: Long): Flow<ItemModel?>

    /**
     * Insert item in the data source
     */
    suspend fun insertItem(item: ItemModel) : Long

    /**
     * Delete item from the data source
     */
    suspend fun deleteItem(item: ItemModel)

    /**
     * Update item in the data source
     */
    suspend fun updateItem(item: ItemModel)
}
