package ar.edu.unlam.mobile.scaffolding.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ShoppingList::class, Item::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun shoppingListDao(): ShoppingListDao

    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        /**
         * Obtiene la instancia de la base de datos.
         */
        fun getDatabase(context: Context): AppDatabase {
            // si la instancia no es nula, la devuelve, sino crea una nueva instancia.
            return Instance ?: synchronized(this) {
                //
                Room
                    .databaseBuilder(context, AppDatabase::class.java, "app_database")
                    // .fallbackToDestructiveMigration() //estrategia para migración
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
