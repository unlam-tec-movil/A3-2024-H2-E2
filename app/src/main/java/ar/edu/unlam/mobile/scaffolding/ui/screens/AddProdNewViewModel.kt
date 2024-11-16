package ar.edu.unlam.mobile.scaffolding.ui.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class AddProdNewViewModel : ViewModel() {
    val itemName = mutableStateOf("")
    val itemQuantity = mutableStateOf("")

    fun clearInputs() {
        itemName.value = ""
        itemQuantity.value = ""
    }
}