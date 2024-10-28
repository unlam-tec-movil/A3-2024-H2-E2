package ar.edu.unlam.mobile.scaffolding.domain.category

import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    /**
     * Obtiene todas las categorías.
     */
    fun getAllCategoriesStream(): Flow<List<CategoryModel>>
}
