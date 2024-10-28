package ar.edu.unlam.mobile.scaffolding.ui.screens

import androidx.lifecycle.ViewModel
import ar.edu.unlam.mobile.scaffolding.data.local.shoppinglist.ShoppingListDao
import ar.edu.unlam.mobile.scaffolding.domain.item.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AddItemsToShoppingListViewModel
    @Inject
    constructor(
        private val shoppingListDao: ShoppingListDao,
        private val itemRepository: ItemRepository,
    ) : ViewModel() {
        private val _checkedStates = MutableStateFlow<MutableMap<Item, Boolean>>(mutableMapOf())
        val checkedStates: StateFlow<MutableMap<Item, Boolean>> = _checkedStates.asStateFlow()

        fun onItemCheckedChange(
            item: Item,
            isChecked: Boolean,
        ) {
            _checkedStates.value =
                _checkedStates.value.toMutableMap().also {
                    it[item] = isChecked
                }
        }
    }
