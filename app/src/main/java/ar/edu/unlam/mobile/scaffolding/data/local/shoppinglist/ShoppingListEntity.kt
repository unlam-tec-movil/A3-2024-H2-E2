package ar.edu.unlam.mobile.scaffolding.data.local.shoppinglist

import androidx.room.Entity
import androidx.room.PrimaryKey
import ar.edu.unlam.mobile.scaffolding.domain.shoppinglist.models.ShoppingListModel

@Entity(tableName = "shopping_lists")
data class ShoppingListEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
)

fun ShoppingListEntity.asModel() =
    ShoppingListModel(
        id = id.toUInt(),
        name = name,
    )

fun ShoppingListModel.asEntity() =
    ShoppingListEntity(
        name = name,
    )
