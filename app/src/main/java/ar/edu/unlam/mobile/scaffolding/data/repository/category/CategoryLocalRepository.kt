package ar.edu.unlam.mobile.scaffolding.data.repository.category

import ar.edu.unlam.mobile.scaffolding.domain.category.CategoryModel
import kotlinx.coroutines.flow.Flow

interface CategoryLocalRepository {
    fun getAllCategoriesStream(): Flow<List<CategoryModel>>
}
