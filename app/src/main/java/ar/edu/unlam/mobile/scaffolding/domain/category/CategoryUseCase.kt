package ar.edu.unlam.mobile.scaffolding.domain.category

import ar.edu.unlam.mobile.scaffolding.data.local.category.CategoryWithItems
import kotlinx.coroutines.flow.Flow

interface CategoriesUseCases {
    fun getAllCategoriesStream(): Flow<List<CategoryModel>> // Todas las categorías

    fun getCategoryWithItemsStream(categoryId: Long): Flow<CategoryWithItems?> // Categoría con ítems
}
