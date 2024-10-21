package ar.edu.unlam.mobile.scaffolding.data.local.item

import androidx.room.Entity
import androidx.room.PrimaryKey
import ar.edu.unlam.mobile.scaffolding.domain.shoppinglist.models.ItemModel

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val quantity: Int,
)

fun ItemEntity.asModel() =
    ItemModel(
        id = id.toUInt(),
        name = name,
        quantity = quantity,
    )

fun ItemModel.asEntity() =
    ItemEntity(
        name = name,
        quantity = quantity,
    )
