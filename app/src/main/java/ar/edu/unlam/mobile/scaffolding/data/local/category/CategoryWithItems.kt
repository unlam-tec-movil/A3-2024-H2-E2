package ar.edu.unlam.mobile.scaffolding.data.local.category

import androidx.room.Embedded
import androidx.room.Relation
import ar.edu.unlam.mobile.scaffolding.data.local.item.ItemEntity
import ar.edu.unlam.mobile.scaffolding.data.local.item.asModel
import ar.edu.unlam.mobile.scaffolding.domain.category.CategoryModel

data class CategoryWithItems(
    @Embedded val category: CategoryEntity,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "category_id_fk",
    )
    val itemsListsByCategory: List<ItemEntity>,
)

fun CategoryWithItems.asCategoryModel(): CategoryModel =
    CategoryModel(
        id = category.categoryId.toULong(),
        name = category.name,
        listItem = itemsListsByCategory.map { it.asModel() },
    )
