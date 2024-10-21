package ar.edu.unlam.mobile.scaffolding.data.local.shoppinglist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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

    @Query("SELECT * FROM shopping_lists WHERE id = :id")
    fun getShoppingListById(id: Int): Flow<ShoppingListEntity>

    @Query("SELECT * FROM shopping_lists")
    fun getAllShoppingLists(): Flow<List<ShoppingListEntity>>
}
