package ar.edu.unlam.mobile.scaffolding.ui.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unlam.mobile.scaffolding.data.local.item.ItemEntity
import ar.edu.unlam.mobile.scaffolding.domain.shoppinglist.ShoppingListsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddProdNewViewModel
    @Inject
    constructor(
        private val shoppingListsUseCases: ShoppingListsUseCases,
        private val savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        // ID de la lista de compras (recuperado del estado guardado o navegación)
        private val listId: Long = checkNotNull(savedStateHandle[ShoppingListDestination.LIST_ID_ARG])

        val itemName = mutableStateOf("")
        val itemQuantity = mutableStateOf("")

        private val _isLoading = MutableStateFlow(false)
        val isLoading: StateFlow<Boolean> = _isLoading

        private val _isSuccess = MutableStateFlow(false)
        val isSuccess: StateFlow<Boolean> = _isSuccess

        private val _errorMessage = MutableStateFlow<String?>(null)
        val errorMessage: StateFlow<String?> = _errorMessage

        /**
         * Limpia los campos de entrada después de una operación exitosa
         */
        fun clearInputs() {
            itemName.value = ""
            itemQuantity.value = ""
        }

        /**
         * Valida los campos de entrada antes de enviar los datos
         */
        private fun validateInputs(): Boolean = itemName.value.isNotBlank() && itemQuantity.value.toIntOrNull() != null

        /**
         * Agregar un producto con su cantidad a la lista de compras actual
         */
        fun addProduct() {
            if (!validateInputs()) {
                _errorMessage.value = "Nombre o cantidad inválidos"
                return
            }

            viewModelScope.launch {
                _isLoading.value = true
                try {
                    // Inserta un nuevo ítem en la lista de compras
                    val productName = itemName.value
                    val quantity = itemQuantity.value.toInt()
                    val categoria = 0

                    // Paso 1: Crear un nuevo producto en la base de datos
                    val newProduct =
                        ItemEntity(
                            name = productName,
                            categoryId = categoria.toLong(), // Aquí puedes manejar categorías si es necesario
                            photo = null, // Opcional
                        )

                    /*val productId =
                        shoppingListsUseCases.addItemToList(
                            listId = shoppingListId,
                            itemId = newProduct.id, // Aquí debes manejar la creación previa del producto
                            quantity = quantity,
                            isChecked = false,
                        )*/

                    // Paso 2: Enlazar el producto con la lista
                    shoppingListsUseCases.addItemToList(
                        listId,
                        itemId = newProduct.id,
                        quantity,
                        isChecked = false,
                    )

                    _isSuccess.value = true
                    clearInputs()
                } catch (e: Exception) {
                    _errorMessage.value = "Error al agregar el producto: ${e.message}"
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }
