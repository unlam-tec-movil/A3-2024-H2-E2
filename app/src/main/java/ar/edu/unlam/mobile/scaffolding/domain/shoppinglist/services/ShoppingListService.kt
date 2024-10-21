package ar.edu.unlam.mobile.scaffolding.domain.shoppinglist.services

import ar.edu.unlam.mobile.scaffolding.domain.shoppinglist.models.ShoppingListModel
import ar.edu.unlam.mobile.scaffolding.domain.shoppinglist.repository.ShoppingListRepository
import ar.edu.unlam.mobile.scaffolding.domain.shoppinglist.usecases.ShoppingListsUseCases
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShoppingListService
    @Inject
    constructor(
        private val shoppingListRepository: ShoppingListRepository,
    ) : ShoppingListsUseCases {
        override suspend fun getAllShoppingLists(): Flow<List<ShoppingListModel>> = shoppingListRepository.getAllShoppingListsStream()

        override suspend fun getShoppingList(id: Int): Flow<ShoppingListModel> = shoppingListRepository.getShoppingListStream(id)

        override suspend fun insertShoppingList(shoppingList: ShoppingListModel) = shoppingListRepository.insertShoppingList(shoppingList)

        override suspend fun deleteShoppingList(shoppingList: ShoppingListModel) = shoppingListRepository.deleteShoppingList(shoppingList)

        override suspend fun updateShoppingList(shoppingList: ShoppingListModel) = shoppingListRepository.updateShoppingList(shoppingList)
    }
