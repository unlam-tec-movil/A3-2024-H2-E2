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
}
