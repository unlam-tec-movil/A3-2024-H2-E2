package ar.edu.unlam.mobile.scaffolding.data.local

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

interface ShoppingListDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(shoppingList: ShoppingList)

    @Update
    suspend fun update(shoppingList: ShoppingList)

    @Delete
    suspend fun delete(shoppingList: ShoppingList)

    @Query("SELECT * FROM shopping_list WHERE id = :id")
    fun getShoppingListById(id: Int): Flow<ShoppingList>

    @Query("SELECT * FROM shopping_list")
    fun getAllShoppingLists(): Flow<List<ShoppingList>>
}
