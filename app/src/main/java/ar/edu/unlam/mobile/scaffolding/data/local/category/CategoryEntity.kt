package ar.edu.unlam.mobile.scaffolding.data.local.category

import androidx.room.Entity
import androidx.room.PrimaryKey
import ar.edu.unlam.mobile.scaffolding.domain.category.CategoryModel

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val categoryId: Long = 0,
    val name: String,
)

fun CategoryEntity.asModel() =
    CategoryModel(
        id = categoryId.toUInt(),
        name = name,
    )

fun CategoryModel.asEntity() =
    CategoryEntity(
        name = name,
    )
