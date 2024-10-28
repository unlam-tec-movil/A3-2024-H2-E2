package ar.edu.unlam.mobile.scaffolding.data.local.shoppinglist

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import ar.edu.unlam.mobile.scaffolding.data.local.item.ItemEntity

data class ShoppingListWithItems(
    @Embedded val shoppingList: ShoppingListEntity,
    @Relation(
        parentColumn = "shopping_list_id",
        entityColumn = "item_id",
        associateBy = Junction(ShoppinglistItemCrossRef::class),
    )
    val itemsByShoppingList: List<ItemEntity>,
)
