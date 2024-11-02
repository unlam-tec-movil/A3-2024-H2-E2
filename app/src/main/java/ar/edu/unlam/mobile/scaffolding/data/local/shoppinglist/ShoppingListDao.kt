package ar.edu.unlam.mobile.scaffolding.data.local.shoppinglist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(shoppingList: ShoppingListEntity)

    @Update
    suspend fun update(shoppingList: ShoppingListEntity)

    @Delete
    suspend fun delete(shoppingList: ShoppingListEntity)

    @Query("SELECT * FROM shopping_lists WHERE shopping_list_id = :id")
    fun getShoppingListById(id: Long): Flow<ShoppingListEntity>

    @Query("SELECT * FROM shopping_lists")
    fun getAllShoppingLists(): Flow<List<ShoppingListEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItemCrossRef(crossRef: ShoppingListItemCrossRef)

    @Query("DELETE FROM ShoppingListItemCrossRef WHERE shopping_list_id = :listId AND item_id = :itemId")
    suspend fun deleteItemCrossRef(
        listId: Long,
        itemId: Long,
    )

    @Transaction
    @Query("SELECT * FROM shopping_lists")
    fun getShoppingListsWithItems(): Flow<List<ShoppingListWithItems>>

    @Transaction
    @Query("SELECT * FROM shopping_lists WHERE shopping_list_id = :id")
    fun getShoppingListWithItems(id: Long): Flow<ShoppingListWithItems>

    @Transaction
    @Query(
        """
    SELECT items.item_id AS id, items.name, items.category_id_fk AS categoryId, 
           crossRef.quantity, crossRef.isChecked
    FROM items
    INNER JOIN ShoppingListItemCrossRef AS crossRef
    ON items.item_id = crossRef.item_id
    WHERE crossRef.shopping_list_id = :listId
""",
    )
    fun getItemsWithQuantityAndCheckedForList(listId: Long): Flow<List<ItemWithQuantityAndChecked>>

    @Query("UPDATE ShoppingListItemCrossRef SET isChecked = :checked WHERE item_id = :itemId AND shopping_list_id = :listId")
    suspend fun updateItemCheckedState(
        itemId: Long,
        listId: Long,
        checked: Boolean,
    )
}
