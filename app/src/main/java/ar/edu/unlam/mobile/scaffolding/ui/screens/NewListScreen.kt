package ar.edu.unlam.mobile.scaffolding.ui.screens

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ar.edu.unlam.mobile.scaffolding.R
import ar.edu.unlam.mobile.scaffolding.ShopListTopAppBar
import ar.edu.unlam.mobile.scaffolding.ui.components.ColorPicker
import ar.edu.unlam.mobile.scaffolding.ui.components.IconPicker
import ar.edu.unlam.mobile.scaffolding.ui.navigation.NavigationDestination
import ar.edu.unlam.mobile.scaffolding.ui.viewmodels.NewListViewModel

object NewListDestination : NavigationDestination {
    override val route = "newList"
    override val titleRes = R.string.crear_nueva_lista
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewListScreen(
    modifier: Modifier = Modifier,
    viewModel: NewListViewModel = hiltViewModel(),
    canNavigateBack: Boolean = true,
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
) {
    val newListState by viewModel.newListState.collectAsState()

    val icons =
        listOf(
            Icons.Default.Favorite,
            Icons.Default.Home,
            Icons.Default.Star,
            Icons.Default.ShoppingCart,
            Icons.Default.Person,
        )

    val colors =
        listOf(
            Color.Red,
            Color.Green,
            Color.Blue,
            Color.Yellow,
            Color(0xFFFFA500),
        )

    // Estado de transición para la animación
    val transitionState = remember { MutableTransitionState(false) }
    transitionState.targetState = true

    val transition = rememberTransition(transitionState, label = "screenFade")
    val alpha by transition.animateFloat(
        label = "alpha",
        transitionSpec = {
            tween(durationMillis = 1500)
        },
    ) { state ->
        if (state) 1f else 0f
    }

    Scaffold(
        topBar = {
            ShopListTopAppBar(
                title = stringResource(NewListDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp,
            )
        },
        bottomBar = {
        },
    ) { innerPadding ->
        //  animación de desvanecimiento al Column__
        Column(
            modifier =
                modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .alpha(alpha),
        ) {
            Text(text = "Crear una nueva lista")

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = newListState.name,
                onValueChange = { viewModel.updateListName(it) },
                label = { Text("Nombre de la lista") },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Seleccionar un color")
            Spacer(modifier = Modifier.height(8.dp))

            ColorPicker(
                colors = colors,
                selectedColor = newListState.selectedColor,
                onColorSelected = { color -> viewModel.updateSelectedColor(color) },
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Seleccionar un ícono")

            Spacer(modifier = Modifier.height(8.dp))

            IconPicker(
                icons = icons,
                selectedIcon = newListState.selectedIcon,
                onIconSelected = { viewModel.updateSelectedIcon(it) },
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.createNewList()
                    navigateBack()
                },
                enabled = viewModel.isFormValid(),
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
            ) {
                Text("Crear lista")
            }
        }
    }
}
