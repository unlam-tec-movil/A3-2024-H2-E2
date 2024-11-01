package ar.edu.unlam.mobile.scaffolding.domain.category

import ar.edu.unlam.mobile.scaffolding.data.local.category.CategoryWithItems
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryService
    @Inject
    constructor(
        private val categoryRepository: CategoryRepository,
    ) : CategoriesUseCases {
        override fun getAllCategoriesStream(): Flow<List<CategoryModel>> = categoryRepository.getAllCategoriesStream()

        override fun getCategoryWithItemsStream(categoryId: Long): Flow<CategoryWithItems?> =
            categoryRepository.getCategoryWithItemsStream(categoryId)
    }
