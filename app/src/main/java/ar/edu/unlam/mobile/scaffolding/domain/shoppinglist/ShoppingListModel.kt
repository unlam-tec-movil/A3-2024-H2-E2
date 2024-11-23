package ar.edu.unlam.mobile.scaffolding.domain.shoppinglist

import ar.edu.unlam.mobile.scaffolding.domain.item.ItemModel

data class ShoppingListModel(
    val id: ULong?,
    val name: String,
    val listItems: List<ItemModel> = emptyList(),
    val selectedImage: Int,
)
