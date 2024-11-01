package ar.edu.unlam.mobile.scaffolding.data.local.shoppinglist

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["shopping_list_id", "item_id"])
data class ShoppingListItemCrossRef(
    @ColumnInfo(name = "shopping_list_id")
    val shoppingListId: Long,
    @ColumnInfo(name = "item_id")
    val itemId: Long,
    val quantity: Int,
    val isChecked: Boolean,
)
