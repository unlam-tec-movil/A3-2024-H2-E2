package ar.edu.unlam.mobile.scaffolding.domain.category

import ar.edu.unlam.mobile.scaffolding.data.local.category.CategoryWithItems
import kotlinx.coroutines.flow.Flow

interface CategoryUseCases {
    /*
     * Retorna todas las categorías con sus ítems
     * Retrieve all the categories from the the given data source.
     */
    fun getAllCategoriesWithItemsStream(): Flow<List<CategoryModel>>

    /*
     * Categoría con ítems
     * Retrieve an category from the given data source that matches with the [id].
     */
    fun getCategoryWithItemsStream(categoryId: Long): Flow<CategoryWithItems?>
}
