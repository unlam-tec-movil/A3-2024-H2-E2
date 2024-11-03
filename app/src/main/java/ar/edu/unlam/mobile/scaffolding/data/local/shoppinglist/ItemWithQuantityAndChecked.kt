package ar.edu.unlam.mobile.scaffolding.data.local.shoppinglist

data class ItemWithQuantityAndChecked(
    val id: Long,
    val name: String,
    val quantity: Int,
    val isChecked: Boolean,
)
