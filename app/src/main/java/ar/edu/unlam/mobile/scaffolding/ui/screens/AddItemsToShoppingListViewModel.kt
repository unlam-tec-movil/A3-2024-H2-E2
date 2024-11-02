package ar.edu.unlam.mobile.scaffolding.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unlam.mobile.scaffolding.domain.category.CategoryModel
import ar.edu.unlam.mobile.scaffolding.domain.category.CategoryUseCases
import ar.edu.unlam.mobile.scaffolding.domain.item.ItemModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AddItemsToShoppingListUIState {
    data class Success(
        val categories: List<CategoryModel>,
    ) : AddItemsToShoppingListUIState

    data object Loading : AddItemsToShoppingListUIState

    data object Error : AddItemsToShoppingListUIState
}

@HiltViewModel
class AddItemsToShoppingListViewModel
    @Inject
    constructor(
        private val service: CategoryUseCases,
    ) : ViewModel() {
        private val _uiState =
            MutableStateFlow<AddItemsToShoppingListUIState>(AddItemsToShoppingListUIState.Loading)
        val uiState: StateFlow<AddItemsToShoppingListUIState> = _uiState.asStateFlow()

        private val _checkedStates = MutableStateFlow<MutableMap<ItemModel, Boolean>>(mutableMapOf())
        val checkedStates: StateFlow<MutableMap<ItemModel, Boolean>> = _checkedStates.asStateFlow()

        init {
            fetchCategoriesWithItems()
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
            _checkedStates.value =
                _checkedStates.value.toMutableMap().also {
                    it[item] = isChecked
                }
        }
    }
