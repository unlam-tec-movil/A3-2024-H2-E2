package ar.edu.unlam.mobile.scaffolding.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ar.edu.unlam.mobile.scaffolding.data.local.category.CategoryDao
import ar.edu.unlam.mobile.scaffolding.data.local.category.CategoryEntity
import ar.edu.unlam.mobile.scaffolding.data.local.item.ItemDao
import ar.edu.unlam.mobile.scaffolding.data.local.item.ItemEntity
import ar.edu.unlam.mobile.scaffolding.data.local.shoppinglist.ShoppingListDao
import ar.edu.unlam.mobile.scaffolding.data.local.shoppinglist.ShoppingListEntity
import ar.edu.unlam.mobile.scaffolding.data.local.shoppinglist.ShoppingListItemCrossRef
import ar.edu.unlam.mobile.scaffolding.data.local.supermerkados.LocationDao
import ar.edu.unlam.mobile.scaffolding.data.local.supermerkados.LocationEntity
import ar.edu.unlam.mobile.scaffolding.data.local.supermerkados.PlaceDao
import ar.edu.unlam.mobile.scaffolding.data.local.supermerkados.PlaceEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val DB_NAME = "shop_database"
val MIGRATION_1_2 =
    object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // Paso 1: Crear la tabla temporal sin las columnas que quieres eliminar

            // Verificar si la tabla places existe, si no, crearla
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS places (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    name TEXT,
                    vicinity TEXT,
                    latitude REAL NOT NULL,
                    longitude REAL  NOT NULL
                );
            """,
            )
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS locations (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    latitude REAL NOT NULL,
                    longitude REAL NOT NULL
                );
            """,
            )

            db.execSQL(
                """
                ALTER TABLE items ADD COLUMN photo TEXT DEFAULT 'undefined'
            """,
            )
        }
    }

@Database(
    entities = [
        ShoppingListEntity::class,
        ItemEntity::class,
        ShoppingListItemCrossRef::class,
        CategoryEntity::class,
        PlaceEntity::class,
        LocationEntity::class,
    ],
    version = 2,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun shoppingListDao(): ShoppingListDao

    abstract fun itemDao(): ItemDao

    abstract fun categoryDao(): CategoryDao

    abstract fun placeDao(): PlaceDao

    abstract fun locationDao(): LocationDao

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
                        ).addMigrations(MIGRATION_1_2)
                        .addCallback(DatabaseCallback(context))
                        .build()
                INSTANCE = instance
                instance
            }

        /** Callback para insertar datos precargados al crear la base de datos */
        private class DatabaseCallback(
            private val context: Context,
        ) : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Ejecutar en un hilo secundario para evitar bloquear el main thread
                CoroutineScope(Dispatchers.IO).launch {
                    prepopulateDatabase(getDatabase(context))
                }
            }
        }

        /**
         * Función para insertar las categorías e ítems al crear la base de datos
         * */
        suspend fun prepopulateDatabase(database: AppDatabase) {
            val categories = DataSource.categoryList

            // Insertar categorías e ítems
            categories.forEach { category ->
                // Insertar categoría y obtener el ID generado
                val categoryEntity = CategoryEntity(name = category.nameCategory)
                val categoryId = database.categoryDao().insert(categoryEntity)

                // Insertar ítems asociados a esta categoría
                category.items.forEach { item ->
                    val itemEntity =
                        ItemEntity(
                            name = item.name,
                            categoryId = categoryId,
                        )
                    database.itemDao().insert(itemEntity)
                }
            }
        }
    }
}
