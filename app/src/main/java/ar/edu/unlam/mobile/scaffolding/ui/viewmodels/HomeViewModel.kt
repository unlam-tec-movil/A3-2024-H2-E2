package ar.edu.unlam.mobile.scaffolding.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unlam.mobile.scaffolding.data.local.shoppinglist.ItemWithQuantityAndChecked
import ar.edu.unlam.mobile.scaffolding.domain.shoppinglist.ShoppingListModel
import ar.edu.unlam.mobile.scaffolding.domain.shoppinglist.ShoppingListsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface HomeUIState {
    data class Success(
        val shoppingLists: List<ShoppingListModel> = emptyList(),
        val isRefreshing: Boolean = false,
        val errorMessage: String? = null,
        val isLoading: Boolean = false,
    ) : HomeUIState

    data object Loading : HomeUIState

    data object Error : HomeUIState
}

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val service: ShoppingListsUseCases,
    ) : ViewModel() {
        // Estado inicial es `Loading`
        private val _uiState = MutableStateFlow<HomeUIState>(HomeUIState.Loading)
        val uiState: StateFlow<HomeUIState> = _uiState

        private val _shoppingListItems = MutableLiveData<List<ItemWithQuantityAndChecked>>()
        val shoppingListItems: LiveData<List<ItemWithQuantityAndChecked>> get() = _shoppingListItems

        init {
            loadShoppingLists()
        }

        // Refrescar listas
        fun refreshShoppingLists() {
            _uiState.value =
                when (val currentState = _uiState.value) {
                    is HomeUIState.Success ->
                        currentState.copy(isRefreshing = true) // Copia del estado actual con refreshing
                    else -> HomeUIState.Loading // Cualquier otro estado, se pone como cargando
                }
            loadShoppingLists()
        }

        // Cargar listas desde el servicio
        private fun loadShoppingLists() {
            viewModelScope.launch {
                _uiState.value = HomeUIState.Loading // Mostrar estado de carga

                try {
                    service.getAllShoppingLists().collect { lists ->
                        Log.d("HomeViewModel", "Listas obtenidas: $lists")
                        if (lists.isNotEmpty()) {
                            // Si hay listas, emitimos el estado de éxito
                            _uiState.value =
                                HomeUIState.Success(
                                    shoppingLists = lists,
                                    isLoading = false,
                                    isRefreshing = false,
                                )
                        } else {
                            // Si la lista está vacía, también puede ser un éxito
                            _uiState.value =
                                HomeUIState.Success(
                                    shoppingLists = emptyList(),
                                    isLoading = false,
                                    isRefreshing = false,
                                )
                        }
                    }
                } catch (e: Exception) {
                    // En caso de error, emitimos el estado de error
                    _uiState.value = HomeUIState.Error
                }
            }
        }

        fun loadShoppingListItems(
            listId: Long,
            onComplete: () -> Unit,
        ) {
            viewModelScope.launch {
                service
                    .getItemsForShoppingList(listId)
                    .collect { items ->
                        _shoppingListItems.value = items
                        onComplete()
                    }
            }
        }
    }
