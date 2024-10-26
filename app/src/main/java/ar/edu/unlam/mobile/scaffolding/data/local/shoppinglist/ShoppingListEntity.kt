package ar.edu.unlam.mobile.scaffolding.data.local.shoppinglist

import androidx.room.Entity
import androidx.room.PrimaryKey
import ar.edu.unlam.mobile.scaffolding.domain.shoppinglist.ShoppingItem
import ar.edu.unlam.mobile.scaffolding.domain.shoppinglist.ShoppingListModel

@Entity(tableName = "shopping_lists")
data class ShoppingListEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val listItems: List<ShoppingItem> = emptyList(),
    val quantity: Int = 0,
    val selectedColor: Int = 0,
    val selectedIcon: Int = 0,
)

fun ShoppingListEntity.asModel() =
    ShoppingListModel(
        id = id.toUInt(),
        name = name,
        listItems = listItems,
        quantity = quantity,
        selectedColor = selectedColor,
        selectedIcon = selectedIcon,
    )

fun ShoppingListModel.asEntity() =
    ShoppingListEntity(
        name = name,
        listItems = listItems,
        quantity = quantity,
        selectedColor = selectedColor,
        selectedIcon = selectedIcon,
    )
