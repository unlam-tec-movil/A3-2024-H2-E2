package ar.edu.unlam.mobile.scaffolding.data.local

import android.content.Context
import ar.edu.unlam.mobile.scaffolding.data.repository.OfflineShoppingListsRepository
import ar.edu.unlam.mobile.scaffolding.data.repository.ShoppingListsRepository

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val shoppingListsRepository: ShoppingListsRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineShoppingListsRepository]
 */
class AppDataContainer(
    private val context: Context,
) : AppContainer {
    /**
     * Implementation for [ShoppingListsRepository]
     */
    override val shoppingListsRepository: ShoppingListsRepository by lazy {
        OfflineShoppingListsRepository(AppDatabase.getDatabase(context).shoppingListDao())
    }
}
