package ar.edu.unlam.mobile.scaffolding.data.local.category

import androidx.room.Embedded
import androidx.room.Relation
import ar.edu.unlam.mobile.scaffolding.data.local.item.ItemEntity

data class CategoryWithItems(
    @Embedded val category: CategoryEntity,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "category_id_fk",
    )
    val itemsListsByCategory: List<ItemEntity>,
)
