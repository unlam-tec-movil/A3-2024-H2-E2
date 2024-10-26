package ar.edu.unlam.mobile.scaffolding.domain.shoppinglist

data class ShoppingListModel(
    val id: UInt?,
    val name: String,
    val listItems: List<ShoppingItem> = emptyList(),
    val quantity: Int = listItems.size,
    val selectedColor: Int,
    val selectedIcon: Int,
)
