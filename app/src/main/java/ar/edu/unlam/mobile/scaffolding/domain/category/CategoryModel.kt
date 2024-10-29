package ar.edu.unlam.mobile.scaffolding.domain.category

import ar.edu.unlam.mobile.scaffolding.domain.item.ItemModel

data class CategoryModel(
    val id: ULong,
    val name: String,
    val listItem: List<ItemModel> = emptyList(),
)
