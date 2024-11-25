package ar.edu.unlam.mobile.scaffolding.ui.viewmodels

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unlam.mobile.scaffolding.domain.shoppinglist.ShoppingListModel
import ar.edu.unlam.mobile.scaffolding.domain.shoppinglist.ShoppingListsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewListViewModel
    @Inject
    constructor(
        private val service: ShoppingListsUseCases,
    ) : ViewModel() {
        private val _newListState = MutableStateFlow(NewListUiState())
        val newListState: StateFlow<NewListUiState> = _newListState

        fun updateListName(name: String) {
            _newListState.update { it.copy(name = name) }
        }

        fun updateSelectedIcon(icon: ImageVector) {
            _newListState.update { it.copy(selectedIcon = icon) }
        }

        fun updateSelectedColor(color: Color) {
            _newListState.update { it.copy(selectedColor = color) }
        }

        fun isFormValid(): Boolean {
            val state = _newListState.value
            return state.name.isNotEmpty() && state.selectedIcon != null && state.selectedColor != null
        }

        fun createNewList() {
            val state = _newListState.value
            if (isFormValid()) {
                val newList =
                    ShoppingListModel(
                        id = null,
                        name = state.name,
                        listItems = emptyList(),
                        selectedColor = state.selectedColor!!.toArgb(),
                        selectedIcon = state.selectedIcon!!.name,
                    )
                viewModelScope.launch {
                    service.insertShoppingList(newList)
                    clearNewListState()
                }
            }
        }

        private fun clearNewListState() {
            _newListState.value = NewListUiState()
        }
    }

data class NewListUiState(
    val name: String = "",
    val selectedColor: Color? = null,
    val selectedIcon: ImageVector? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
