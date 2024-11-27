package ar.edu.unlam.mobile.scaffolding.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unlam.mobile.scaffolding.R
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

        fun isFormValid(): Boolean {
            val state = _newListState.value
            return state.name.isNotEmpty()
        }

        fun createNewList() {
            val state = _newListState.value
            if (isFormValid()) {
                val newList =
                    ShoppingListModel(
                        id = null,
                        name = state.name,
                        listItems = emptyList(),
                        selectedImage = state.selectedImage,
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

        fun updateSelectedImage(imageRes: Int) {
            _newListState.value = _newListState.value.copy(selectedImage = imageRes)
        }
    }

data class NewListUiState(
    val name: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val selectedImage: Int = R.drawable.image0,
)
