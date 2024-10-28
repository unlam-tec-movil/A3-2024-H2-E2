package ar.edu.unlam.mobile.scaffolding.domain.shoppinglist

import ar.edu.unlam.mobile.scaffolding.domain.item.ItemModel

data class ShoppingListModel(
    val id: UInt?,
    val name: String,
    val listItems: List<ItemModel> = emptyList(),
    val quantity: Int = listItems.size,
    val selectedColor: Int,
    val selectedIcon: Int,
)
