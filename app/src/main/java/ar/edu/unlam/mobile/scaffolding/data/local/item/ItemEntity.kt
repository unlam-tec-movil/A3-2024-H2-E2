package ar.edu.unlam.mobile.scaffolding.data.local.item

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ar.edu.unlam.mobile.scaffolding.domain.item.ItemModel

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "item_id")
    val id: Long = 0,
    val name: String,
    @ColumnInfo(name = "category_id_fk")
    val categoryId: Long,
)

fun ItemEntity.asModel() =
    ItemModel(
        id = id.toUInt(),
        name = name,
        categoryId = categoryId,
    )

fun ItemModel.asEntity() =
    ItemEntity(
        name = name,
        categoryId = categoryId,
    )
