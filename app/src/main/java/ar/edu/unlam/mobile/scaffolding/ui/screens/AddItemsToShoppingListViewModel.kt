package ar.edu.unlam.mobile.scaffolding.ui.screens

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlin.collections.toMutableMap

class AddItemsToShoppingListViewModel
@Inject
constructor() : ViewModel() {
    private val _checkedStates = MutableStateFlow<MutableMap<Item, Boolean>>(mutableMapOf())
    val checkedStates: StateFlow<MutableMap<Item, Boolean>> = _checkedStates.asStateFlow()

    fun onItemCheckedChange(item: Item, isChecked: Boolean) {
        _checkedStates.value = _checkedStates.value.toMutableMap().also {
            it[item] = isChecked
        }
    }
}