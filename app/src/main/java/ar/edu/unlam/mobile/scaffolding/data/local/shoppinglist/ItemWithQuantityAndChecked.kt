package ar.edu.unlam.mobile.scaffolding.data.local.shoppinglist

data class ItemWithQuantityAndChecked(
    val id: Long,
    val name: String,
    var quantity: Int,
    var isChecked: Boolean,
    var photo: String?,
)
