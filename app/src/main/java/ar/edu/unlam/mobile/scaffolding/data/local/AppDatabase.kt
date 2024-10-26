package ar.edu.unlam.mobile.scaffolding.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ar.edu.unlam.mobile.scaffolding.data.local.item.ItemDao
import ar.edu.unlam.mobile.scaffolding.data.local.item.ItemEntity
import ar.edu.unlam.mobile.scaffolding.data.local.shoppinglist.ShoppingItemConverters
import ar.edu.unlam.mobile.scaffolding.data.local.shoppinglist.ShoppingListDao
import ar.edu.unlam.mobile.scaffolding.data.local.shoppinglist.ShoppingListEntity

@Database(
    entities = [ShoppingListEntity::class, ItemEntity::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(ShoppingItemConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun shoppingListDao(): ShoppingListDao

    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                val instance =
                    Room
                        .databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "app_database",
                        ).build()
                INSTANCE = instance
                instance
            }
    }
}
