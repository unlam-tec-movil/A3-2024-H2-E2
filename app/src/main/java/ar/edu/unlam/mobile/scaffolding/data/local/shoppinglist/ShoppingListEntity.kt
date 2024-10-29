package ar.edu.unlam.mobile.scaffolding.data.local.shoppinglist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ar.edu.unlam.mobile.scaffolding.domain.shoppinglist.ShoppingListModel

@Entity(tableName = "shopping_lists")
data class ShoppingListEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "shopping_list_id")
    val id: Long = 0,
    val name: String,
    val quantity: Int = 0,
    val selectedColor: Int = 0,
    val selectedIcon: Int = 0,
)

fun ShoppingListEntity.asModel() =
    ShoppingListModel(
        id = id.toULong(),
        name = name,
        selectedColor = selectedColor,
        selectedIcon = selectedIcon,
    )

fun ShoppingListModel.asEntity() =
    ShoppingListEntity(
        name = name,
        selectedColor = selectedColor,
        selectedIcon = selectedIcon,
    )
