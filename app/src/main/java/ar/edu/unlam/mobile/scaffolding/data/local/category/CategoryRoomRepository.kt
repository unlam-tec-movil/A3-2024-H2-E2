package ar.edu.unlam.mobile.scaffolding.data.local.category

import ar.edu.unlam.mobile.scaffolding.data.local.AppDatabase
import ar.edu.unlam.mobile.scaffolding.data.repository.category.CategoryLocalRepository
import ar.edu.unlam.mobile.scaffolding.domain.category.CategoryModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRoomRepository
    @Inject
    constructor(
        appDatabase: AppDatabase,
    ) : CategoryLocalRepository {
        private val categoryDao = appDatabase.categoryDao()

    /*
     * Retorna una lista de categorías con sus ítems en un flujo.
     * */
        override fun getAllCategoriesWithItemsStream(): Flow<List<CategoryModel>> =
            categoryDao.getCategoriesWithItems().map { categoriesWithItems ->
                categoriesWithItems.map { it.asCategoryModel() }
            }

        override fun getCategoryWithItemsStream(categoryId: Long): Flow<CategoryWithItems> =
            categoryDao.getCategoriesWithItems().map { categories ->
                categories.first { it.category.categoryId == categoryId }
            }
    }
