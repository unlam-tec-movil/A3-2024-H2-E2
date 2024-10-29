package ar.edu.unlam.mobile.scaffolding.data.repository.category

import ar.edu.unlam.mobile.scaffolding.data.local.category.CategoryWithItems
import ar.edu.unlam.mobile.scaffolding.domain.category.CategoryModel
import ar.edu.unlam.mobile.scaffolding.domain.category.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryDefaultRepository
    @Inject
    constructor(
        private val local: CategoryLocalRepository,
    ) : CategoryRepository {
        override fun getAllCategoriesStream(): Flow<List<CategoryModel>> = local.getAllCategoriesStream()

        override fun getCategoryWithItemsStream(categoryId: Long): Flow<CategoryWithItems> = local.getCategoryWithItemsStream(categoryId)
    }
