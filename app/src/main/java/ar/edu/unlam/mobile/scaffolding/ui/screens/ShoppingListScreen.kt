package ar.edu.unlam.mobile.scaffolding.ui.screens

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ar.edu.unlam.mobile.scaffolding.data.local.shoppinglist.ItemWithQuantityAndChecked
import ar.edu.unlam.mobile.scaffolding.ui.navigation.NavigationDestination
import ar.edu.unlam.mobile.scaffolding.ui.theme.AppTheme

object ShoppingListDestination : NavigationDestination {
    override val route = "shopping_list"
    override val titleRes = 0
    const val LIST_ID_ARG = "listId"
    val routeWithArgs = "$route/{$LIST_ID_ARG}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: ShoppingListViewModel = hiltViewModel(),
) {
    val listId =
        navController.currentBackStackEntry?.arguments?.getLong(ShoppingListDestination.LIST_ID_ARG)
    navController.currentBackStackEntry?.savedStateHandle?.set(
        ShoppingListDestination.LIST_ID_ARG,
        listId,
    )

    val transitionState = remember { MutableTransitionState(false) }
    transitionState.targetState = true

    val transition = updateTransition(targetState = transitionState.targetState, label = "screenFade")
    val alpha by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 1500) },
        label = "alpha",
    ) { state ->
        if (state) 1f else 0f
    }

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista 2") },
                navigationIcon = {
                    IconButton(onClick = { /* Volver a la pantalla anterior */ }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
            )
        },
        content = { paddingValues ->
            when (uiState) {
                is ShoppingListUIState.Loading -> LoadingScreen()

                is ShoppingListUIState.Error -> ErrorMessage(message = "Error al cargar listas")

                is ShoppingListUIState.Success -> {
                    val itemsList = (uiState as ShoppingListUIState.Success).itemLists
                    ShoppingListBody(
                        itemsList = itemsList,
                        viewModel = viewModel,
                        modifier =
                            modifier
                                .fillMaxSize()
                                .alpha(alpha)
                                .padding(paddingValues),
                    )
                }
            }
        },
    )
}

/*@Composable
fun ShoppingListScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: ShoppingListViewModel = hiltViewModel(),
) {
    val listId =
        navController.currentBackStackEntry?.arguments?.getLong(ShoppingListDestination.LIST_ID_ARG)
    navController.currentBackStackEntry?.savedStateHandle?.set(
        ShoppingListDestination.LIST_ID_ARG,
        listId,
    )

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

    Log.d("ListId", "listId en ShoppingListScreen: $listId")
    val uiState by viewModel.uiState.collectAsState()

    when (uiState) {
        is ShoppingListUIState.Loading -> LoadingScreen()

        is ShoppingListUIState.Error -> ErrorMessage(message = "Error al cargar listas")

        is ShoppingListUIState.Success -> {
            val itemsList = (uiState as ShoppingListUIState.Success).itemLists
            ShoppingListBody(
                itemsList = itemsList,
                viewModel = viewModel,
                modifier = modifier.fillMaxSize().alpha(alpha),
            )
        }
    }
}*/

@Composable
fun ShoppingListBody(
    itemsList: List<ItemWithQuantityAndChecked>,
    viewModel: ShoppingListViewModel,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        if (itemsList.isEmpty()) {
            Text(
                text = "Lista de compras vacía, agregue un producto",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(contentPadding),
            )
        } else {
            ShoppingListItems(itemsList = itemsList, viewModel = viewModel)
        }
    }
}

@Composable
fun ShoppingListItems(
    itemsList: List<ItemWithQuantityAndChecked>,
    viewModel: ShoppingListViewModel,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        items(itemsList) { item ->
            ItemRow(
                item = item,
                onCheckedChange = { isChecked ->
                    viewModel.updateItemCheckedState(
                        itemId = item.id,
                        isChecked = isChecked,
                    )
                },
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}

@Composable
fun ItemRow(
    item: ItemWithQuantityAndChecked,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            Checkbox(
                checked = item.isChecked,
                onCheckedChange = { isChecked -> onCheckedChange(isChecked) },
                modifier = Modifier.padding(0.dp),
            )
            Text(
                text = item.name,
                textDecoration =
                    if (item.isChecked) {
                        TextDecoration.LineThrough
                    } else {
                        null
                    },
            )

            Spacer(modifier = Modifier.weight(2f))
            // Text("Menor precio en la tienda")
            Spacer(modifier = Modifier.weight(0.5f))
            Text("x" + item.quantity)

            /*Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { quantity-- }) {
                    Icon(Icons.Filled.Remove, contentDescription = "Disminuir cantidad")
                }
                Text(text = quantity.toString())
                IconButton(onClick = { quantity++ }) {
                    Icon(Icons.Filled.Add, contentDescription = "Aumentar cantidad")
                }
            }*/
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    AppTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            ShoppingListScreen(navController = NavController(LocalContext.current))
        }
    }
}
