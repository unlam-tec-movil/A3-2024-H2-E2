package ar.edu.unlam.mobile.scaffolding.data.local.category

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Transaction
    @Query("SELECT * FROM categories")
    fun getCategoriesWithItems(): Flow<List<CategoryWithItems>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(category: CategoryEntity): Long
}
