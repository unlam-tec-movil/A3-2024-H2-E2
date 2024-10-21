package ar.edu.unlam.mobile.scaffolding.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ar.edu.unlam.mobile.scaffolding.data.local.item.ItemDao
import ar.edu.unlam.mobile.scaffolding.data.local.item.ItemEntity
import ar.edu.unlam.mobile.scaffolding.data.local.shoppinglist.ShoppingListDao
import ar.edu.unlam.mobile.scaffolding.data.local.shoppinglist.ShoppingListEntity

@Database(
    entities = [ShoppingListEntity::class, ItemEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun shoppingListDao(): ShoppingListDao

    abstract fun itemDao(): ItemDao
}
