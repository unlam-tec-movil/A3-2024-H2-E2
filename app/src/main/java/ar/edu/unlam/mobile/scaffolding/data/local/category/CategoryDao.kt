package ar.edu.unlam.mobile.scaffolding.data.local.category

import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

interface CategoryDao {
    @Transaction
    @Query("SELECT * FROM categories")
    fun getCategoriesWithItems(): Flow<List<CategoryWithItems>>

    @Insert
    fun insert(category: CategoryEntity)
}
