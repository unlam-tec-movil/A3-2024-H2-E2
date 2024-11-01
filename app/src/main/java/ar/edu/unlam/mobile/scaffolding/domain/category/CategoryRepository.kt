package ar.edu.unlam.mobile.scaffolding.domain.category

import ar.edu.unlam.mobile.scaffolding.data.local.category.CategoryWithItems
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    /**
     * Retrieve all the categories from the the given data source.
     */
    fun getAllCategoriesStream(): Flow<List<CategoryModel>>

    /**
     * Retrieve an category from the given data source that matches with the [categoryId] with its items.
     */
    fun getCategoryWithItemsStream(categoryId: Long): Flow<CategoryWithItems?>
}
