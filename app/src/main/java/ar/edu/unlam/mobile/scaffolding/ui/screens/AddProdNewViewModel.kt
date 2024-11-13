package ar.edu.unlam.mobile.scaffolding.ui.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddProdNewViewModel : ViewModel() {
    private val nombre = MutableLiveData("")
    val prodName: LiveData<String> = nombre

    private val cantidad = MutableLiveData("")
    val prodCantidad: LiveData<String> = cantidad

    fun onItemNameChange(newName: String) {
        nombre.value = newName
    }

    fun onItemQuantityChange(newQuantity: String) {
        cantidad.value = newQuantity
    }

    fun clearFields() {
        nombre.value = ""
        cantidad.value = ""
    }
}
