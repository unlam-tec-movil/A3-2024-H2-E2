package ar.edu.unlam.mobile.scaffolding.ui.screens

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unlam.mobile.scaffolding.data.local.shoppinglist.ItemWithQuantityAndChecked
import ar.edu.unlam.mobile.scaffolding.domain.shoppinglist.ShoppingListsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ShoppingListUIState {
    data class Success(
        val itemLists: List<ItemWithQuantityAndChecked> = emptyList(),
        val isRefreshing: Boolean = false,
        val errorMessage: String? = null,
        val isLoading: Boolean = false,
    ) : ShoppingListUIState

    data object Loading : ShoppingListUIState

    data class Error(
        val message: String,
    ) : ShoppingListUIState
}

@HiltViewModel
class ShoppingListViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val service: ShoppingListsUseCases,
    ) : ViewModel() {
        private val listId: Long = checkNotNull(savedStateHandle[ShoppingListDestination.LIST_ID_ARG])
        private val _uiState = MutableStateFlow<ShoppingListUIState>(ShoppingListUIState.Loading)
        val uiState: StateFlow<ShoppingListUIState> = _uiState

        init {
            loadShoppingListItems(listId)
            Log.d("ListId", "listId en shoppingViewModel: $listId")
        }

        fun refreshShoppingListItems(listId: Long) {
            _uiState.value = (_uiState.value as? ShoppingListUIState.Success)?.copy(isRefreshing = true)
                ?: ShoppingListUIState.Loading
            loadShoppingListItems(listId)
        }

        private fun loadShoppingListItems(listId: Long) {
            viewModelScope
                .launch {
                    service.getItemsForShoppingList(listId).collect { itemsWithDetails ->
                        _uiState.value =
                            ShoppingListUIState.Success(
                                itemLists =
                                    itemsWithDetails.map { itemWithDetails ->
                                        ItemWithQuantityAndChecked(
                                            id = itemWithDetails.id,
                                            name = itemWithDetails.name,
                                            quantity = itemWithDetails.quantity,
                                            isChecked = itemWithDetails.isChecked,
                                        )
                                    },
                            )
                        Log.d("ShoppingListViewModel", "ItemLists: ${_uiState.value}")
                    }
                }.invokeOnCompletion { throwable ->
                    if (throwable != null) {
                        _uiState.value =
                            ShoppingListUIState.Error("Error loading items: ${throwable.message}")
                    }
                }
        }

        fun updateItemCheckedState(
            itemId: Long,
            isChecked: Boolean,
        ) {
            viewModelScope.launch {
                service.updateItemCheckedState(itemId, listId, isChecked)
            }
        }
    }
