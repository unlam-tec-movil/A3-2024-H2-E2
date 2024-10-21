package ar.edu.unlam.mobile.scaffolding.data.local.item

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: ItemEntity)

    @Update
    suspend fun update(item: ItemEntity)

    @Delete
    suspend fun delete(item: ItemEntity)

    @Query("SELECT * FROM items WHERE id = :id")
    fun getItemById(id: Int): Flow<ItemEntity>

    @Query("SELECT * FROM items")
    fun getAllItems(): Flow<List<ItemEntity>>
}
