package ar.edu.unlam.mobile.scaffolding.data.repository.category

import ar.edu.unlam.mobile.scaffolding.data.local.category.CategoryWithItems
import ar.edu.unlam.mobile.scaffolding.domain.category.CategoryModel
import kotlinx.coroutines.flow.Flow

interface CategoryLocalRepository {
    /**
     * Retrieve all the categories from the the given data source.
     */
    fun getAllCategoriesStream(): Flow<List<CategoryModel>>

    /**
     * Retrieve an category from the given data source that matches with the [categoryId] with its items.
     */
    fun getCategoryWithItemsStream(categoryId: Long): Flow<CategoryWithItems>
}
