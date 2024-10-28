package ar.edu.unlam.mobile.scaffolding.data.repository.category

import ar.edu.unlam.mobile.scaffolding.domain.category.CategoryRepository
import javax.inject.Inject

class CategoryDefaultRepository
    @Inject
    constructor(
        private val local: CategoryLocalRepository,
    ) : CategoryRepository {
    }
