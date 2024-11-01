package ar.edu.unlam.mobile.scaffolding.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import ar.edu.unlam.mobile.scaffolding.data.local.category.CategoryDao
import ar.edu.unlam.mobile.scaffolding.data.local.category.CategoryEntity
import ar.edu.unlam.mobile.scaffolding.data.local.item.ItemDao
import ar.edu.unlam.mobile.scaffolding.data.local.item.ItemEntity
import ar.edu.unlam.mobile.scaffolding.data.local.shoppinglist.ShoppingListDao
import ar.edu.unlam.mobile.scaffolding.data.local.shoppinglist.ShoppingListEntity
import ar.edu.unlam.mobile.scaffolding.data.local.shoppinglist.ShoppingListItemCrossRef
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val DB_NAME = "shop_database"

@Database(
    entities = [ShoppingListEntity::class, ItemEntity::class, ShoppingListItemCrossRef::class, CategoryEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun shoppingListDao(): ShoppingListDao

    abstract fun itemDao(): ItemDao

    abstract fun categoryDao(): CategoryDao

    companion object {
        @Suppress("ktlint:standard:property-naming")
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                val instance =
                    Room
                        .databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            DB_NAME,
                        ).addCallback(DatabaseCallback(context))
                        .build()
                INSTANCE = instance
                instance
            }

        /** Callback para insertar datos precargados al crear la base de datos */
        private class DatabaseCallback(
            private val context: Context,
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Ejecutar en un hilo secundario para evitar bloquear el main thread
                CoroutineScope(Dispatchers.IO).launch {
                    prepopulateDatabase(getDatabase(context))
                }
            }
        }

        /** Función para insertar las categorías e ítems al crear la base de datos */
        suspend fun prepopulateDatabase(database: AppDatabase) {
            val (categories, items) = DataSource.toCategoryAndItemEntities()

            // Insertar categorías en la base de datos y guardar los IDs generados
            categories.forEach { category ->
                val categoryId: Long = database.categoryDao().insert(category) // ID correcto

                // Filtrar ítems que pertenecen a esta categoría
                val itemsForCategory = items.filter { it.categoryId == category.categoryId }

                // Insertar ítems con el nuevo ID de categoría
                itemsForCategory.forEach { item ->
                    database.itemDao().insert(
                        item.copy(categoryId = categoryId),
                    )
                }
            }
        }
    }
}
