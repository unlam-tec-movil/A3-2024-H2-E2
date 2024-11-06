package ar.edu.unlam.mobile.scaffolding.ui.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddProdNewViewModel : ViewModel() {
    private val _itemName = MutableLiveData("")
    val itemName: LiveData<String> = _itemName

    private val _itemQuantity = MutableLiveData("")
    val itemQuantity: LiveData<String> = _itemQuantity

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun onItemNameChange(newItemName: String) {
        _itemName.value = newItemName
    }

    fun onItemQuantityChange(newQuantity: String) {
        _itemQuantity.value = newQuantity
    }

    fun addItem() {
        if (_itemName.value.isNullOrBlank() || _itemQuantity.value.isNullOrBlank()) {
            _message.value = "Por favor, completa todos los campos"
        } else {
            _message.value = "Item agregado: ${_itemName.value} - Cantidad: ${_itemQuantity.value}"
            _itemName.value = ""
            _itemQuantity.value = ""
        }
    }
}
