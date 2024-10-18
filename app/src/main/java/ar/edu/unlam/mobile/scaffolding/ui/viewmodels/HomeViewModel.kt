package ar.edu.unlam.mobile.scaffolding.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import ar.edu.unlam.mobile.scaffolding.ui.screens.CardItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@Immutable
sealed interface HelloMessageUIState {
    data class Success(
        val message: String,
    ) : HelloMessageUIState

    data object Loading : HelloMessageUIState

    data class Error(
        val message: String,
    ) : HelloMessageUIState
}

data class HomeUIState(
    val helloMessageState: HelloMessageUIState,
)

@HiltViewModel
class HomeViewModel
    @Inject
    constructor() : ViewModel() {
        // Mutable State Flow contiene un objeto de estado mutable. Simplifica la operación de
        // actualización de información y de manejo de estados de una aplicación: Cargando, Error, Éxito
        // (https://developer.android.com/kotlin/flow/stateflow-and-sharedflow)
        // _helloMessage State es el estado del componente "HelloMessage" inicializado como "Cargando"
        private val helloMessage = MutableStateFlow(HelloMessageUIState.Loading)

    private val _listItems = MutableStateFlow<List<CardItem>>(emptyList())
    val listItems: StateFlow<List<CardItem>> = _listItems

        // _Ui State es el estado general del view model.
        private val _uiState =
            MutableStateFlow(
                HomeUIState(helloMessage.value),
            )

        // UIState expone el estado anterior como un Flujo de Estado de solo lectura.
        // Esto impide que se pueda modificar el estado desde fuera del ViewModel.
        val uiState = _uiState.asStateFlow()

        init {
            _uiState.value = HomeUIState(HelloMessageUIState.Success("2b"))
        }

        fun addNewList(title: String, color: androidx.compose.ui.graphics.Color, icon: ImageVector) {
            val newItem = CardItem(title, 0, null, color, icon)
            _listItems.value += newItem
            Log.d("HomeViewModel", "Lista actualizada: ${_listItems.value}")
        }
    }
