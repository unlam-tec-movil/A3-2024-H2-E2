package ar.edu.unlam.mobile.scaffolding.ui.screens

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unlam.mobile.scaffolding.data.local.shoppinglist.ItemWithQuantityAndChecked
import ar.edu.unlam.mobile.scaffolding.domain.category.CategoryModel
import ar.edu.unlam.mobile.scaffolding.domain.category.CategoryUseCases
import ar.edu.unlam.mobile.scaffolding.domain.item.ItemModel
import ar.edu.unlam.mobile.scaffolding.domain.shoppinglist.ShoppingListsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AddItemsToShoppingListUIState {
    data class Success(
        val categories: List<CategoryModel>,
    ) : AddItemsToShoppingListUIState

    data object Loading : AddItemsToShoppingListUIState

    data object Error : AddItemsToShoppingListUIState
}

data class ItemState(
    val isChecked: Boolean = false,
    val quantity: Int = 0,
)

@HiltViewModel
class AddItemsToShoppingListViewModel
    @Inject
    constructor(
        private val service: CategoryUseCases,
        private val shoppingListService: ShoppingListsUseCases,
        private val savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private val temporaryItems = mutableListOf<ItemWithQuantityAndChecked>()
        private val listId: Long = checkNotNull(savedStateHandle[ShoppingListDestination.LIST_ID_ARG])

        // StateFlow para el UI State
        private val _uiState =
            MutableStateFlow<AddItemsToShoppingListUIState>(AddItemsToShoppingListUIState.Loading)
        val uiState: StateFlow<AddItemsToShoppingListUIState> =
            _uiState
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = AddItemsToShoppingListUIState.Loading,
                )

        // StateFlow para los estados de los ítems
        private val _itemStates = MutableStateFlow<MutableMap<ItemModel, ItemState>>(mutableMapOf())
        val itemStates: StateFlow<Map<ItemModel, ItemState>> =
            _itemStates
                .map { it.toMap() }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = emptyMap(),
                )

        init {
            fetchCategoriesWithItems()
            Log.d("ListId", "ListId en AdditemsViewModel: $listId")
            savedStateHandle.get<List<ItemWithQuantityAndChecked>>("temporaryItemsListKey")?.let {
                temporaryItems.addAll(it)
            }
        }

        private fun fetchCategoriesWithItems() {
            viewModelScope.launch {
                service.getAllCategoriesWithItemsStream().collect { categories ->
                    _uiState.value = AddItemsToShoppingListUIState.Success(categories)
                }
            }
        }

        fun onItemCheckedChange(
            item: ItemModel,
            isChecked: Boolean,
        ) {
            _itemStates.update { currentMap ->
                currentMap.toMutableMap().apply {
                    val currentState = this[item] ?: ItemState()
                    this[item] = currentState.copy(isChecked = isChecked)
                }
            }
            updateTemporaryItems(item)
        }

        fun addOne(item: ItemModel) {
            _itemStates.update { currentMap ->
                currentMap.toMutableMap().apply {
                    val currentState = this[item] ?: ItemState()
                    this[item] = currentState.copy(quantity = currentState.quantity + 1)
                }
            }
            updateTemporaryItems(item)
        }

        fun subtractOne(item: ItemModel) {
            _itemStates.update { currentMap ->
                currentMap.toMutableMap().apply {
                    val currentState = this[item] ?: ItemState()
                    if (currentState.quantity > 0) {
                        this[item] = currentState.copy(quantity = currentState.quantity - 1)
                    }
                }
            }
            updateTemporaryItems(item)
        }

        private fun updateTemporaryItems(item: ItemModel) {
            val currentState = _itemStates.value[item] ?: return
            val index = temporaryItems.indexOfFirst { it.id == item.id!!.toLong() }

            if (currentState.isChecked && currentState.quantity > 0) {
                if (index != -1) {
                    temporaryItems[index].apply {
                        quantity = currentState.quantity
                        isChecked = currentState.isChecked
                    }
                } else {
                    item.id?.let {
                        temporaryItems.add(
                            ItemWithQuantityAndChecked(
                                id = it.toLong(),
                                name = item.name,
                                quantity = currentState.quantity,
                                isChecked = currentState.isChecked,
                                photo = null,
                            ),
                        )
                    }
                }
            } else if (index != -1) {
                temporaryItems.removeAt(index)
            }
        }

        fun saveItemsToShoppingList() {
            viewModelScope.launch {
                if (temporaryItems.isNotEmpty()) {
                    temporaryItems.forEach { item ->
                        Log.d("ListId", "listId en saveItemsToShoppingList: $listId")
                        shoppingListService.addItemToList(
                            listId = listId,
                            itemId = item.id,
                            quantity = item.quantity,
                            isChecked = false,
                        )
                    }
                }
            }
        }

        fun hasSelectedItems(): Boolean = _itemStates.value.any { it.value.isChecked }

        override fun onCleared() {
            savedStateHandle["temporaryItemsListKey"] = temporaryItems
            super.onCleared()
        }
    }
