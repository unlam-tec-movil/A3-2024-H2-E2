package ar.edu.unlam.mobile.scaffolding.data.local.category

import ar.edu.unlam.mobile.scaffolding.data.local.AppDatabase
import ar.edu.unlam.mobile.scaffolding.data.repository.category.CategoryLocalRepository
import ar.edu.unlam.mobile.scaffolding.domain.category.CategoryModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryRoomRepository
    @Inject
    constructor(
        private val appDatabase: AppDatabase,
    ) : CategoryLocalRepository {
        private val categoryDao = appDatabase.categoryDao()

        override fun getAllCategoriesStream(): Flow<List<CategoryModel>> {
            categoryDao.getCategoriesWithItems()
        }
    }
