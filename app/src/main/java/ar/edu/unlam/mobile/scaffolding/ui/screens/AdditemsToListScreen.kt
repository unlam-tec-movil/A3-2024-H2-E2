package ar.edu.unlam.mobile.scaffolding.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ar.edu.unlam.mobile.scaffolding.domain.category.CategoryModel
import ar.edu.unlam.mobile.scaffolding.domain.item.ItemModel
import ar.edu.unlam.mobile.scaffolding.ui.navigation.AppScreens
import ar.edu.unlam.mobile.scaffolding.ui.navigation.NavigationDestination

object AdditemsDestination : NavigationDestination {
    override val route = "addItemsToList"
    override val titleRes = 0
    const val LIST_ID_ARG = "listId"
    val routeWithArgs = "$route/{$LIST_ID_ARG}"
}

@Composable
fun AddItemsToShoppingListScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: AddItemsToShoppingListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val itemStates = viewModel.itemStates.collectAsState()

    val transitionState = remember { MutableTransitionState(false) }
    transitionState.targetState = true

    val transition = updateTransition(targetState = transitionState.targetState, label = "screenFade")
    val alpha by transition.animateFloat(
        label = "alpha",
        transitionSpec = { tween(durationMillis = 1500) },
    ) { state ->
        if (state) 1f else 0f
    }

    BackHandler {
        viewModel.saveItemsToShoppingList()
        navController.popBackStack()
    }

    Scaffold(
        floatingActionButton = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.End,
                modifier = Modifier.padding(16.dp),
            ) {
                FloatingActionButton(onClick = {
                    // Lógica para agregar un nuevo ítem o lista
                    navController.navigate(route = AppScreens.AddNewProd.route)
                }) {
                    Icon(Icons.Filled.Add, contentDescription = "Agregar producto")
                }
            }
        },
        content = { paddingValues ->
            when (uiState) {
                is AddItemsToShoppingListUIState.Loading ->
                    Text(
                        text = "Cargando categorías...",
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .padding(paddingValues),
                        textAlign = TextAlign.Center,
                    )
                is AddItemsToShoppingListUIState.Error ->
                    Text(
                        text = "Error al cargar los datos",
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .padding(paddingValues),
                        textAlign = TextAlign.Center,
                    )
                is AddItemsToShoppingListUIState.Success -> {
                    val categories = (uiState as AddItemsToShoppingListUIState.Success).categories
                    AddItemsBody(
                        categoryList = categories,
                        modifier =
                            modifier
                                .fillMaxWidth()
                                .alpha(alpha)
                                .padding(paddingValues),
                        itemStates = itemStates.value,
                        onItemCheckedChange = { item, isChecked ->
                            viewModel.onItemCheckedChange(item, isChecked)
                        },
                        onPlusQuantity = { item ->
                            viewModel.addOne(item)
                        },
                        onSustractQuantity = { item ->
                            viewModel.subtractOne(item)
                        },
                    )
                }
            }
        },
    )
}

/*@Composable
fun AddItemsToShoppingListScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: AddItemsToShoppingListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val itemStates = viewModel.itemStates.collectAsState()

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

    BackHandler {
        // Intercept the back press event
        viewModel.saveItemsToShoppingList()
        navController.popBackStack() // Allow default back navigation
    }

    when (uiState) {
        is AddItemsToShoppingListUIState.Loading -> Text("Cargando categorías...")
        is AddItemsToShoppingListUIState.Error -> Text("Error al cargar los datos")
        is AddItemsToShoppingListUIState.Success -> {
            val categories = (uiState as AddItemsToShoppingListUIState.Success).categories
            AddItemsBody(
                categoryList = categories,
                modifier = modifier.fillMaxWidth().alpha(alpha),
                itemStates = itemStates.value,
                onItemCheckedChange = { item, isChecked ->
                    viewModel.onItemCheckedChange(item, isChecked)
                },
                onPlusQuantity = { item ->
                    viewModel.addOne(item)
                },
                onSustractQuantity = { item ->
                    viewModel.subtractOne(item)
                },
            )
        }
    }
}*/

@Composable
fun AddItemsBody(
    categoryList: List<CategoryModel>,
    modifier: Modifier = Modifier,
    itemStates: Map<ItemModel, ItemState>,
    onItemCheckedChange: (ItemModel, Boolean) -> Unit,
    onPlusQuantity: (ItemModel) -> Unit,
    onSustractQuantity: (ItemModel) -> Unit,
) {
    Column(modifier = modifier) {
        LazyColumn {
            items(categoryList.size) { index ->
                CategoryItem(
                    category = categoryList[index],
                    itemStates = itemStates,
                    onItemCheckedChange = onItemCheckedChange,
                    onPlusQuantity = onPlusQuantity,
                    onSustractQuantity = onSustractQuantity,
                    modifier = Modifier.padding(8.dp),
                )
            }
        }
    }
}

@Composable
fun CategoryItem(
    category: CategoryModel,
    itemStates: Map<ItemModel, ItemState>,
    onItemCheckedChange: (ItemModel, Boolean) -> Unit,
    onPlusQuantity: (ItemModel) -> Unit,
    onSustractQuantity: (ItemModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val color by animateColorAsState(
        targetValue = if (expanded) MaterialTheme.colorScheme.surfaceContainerHighest else MaterialTheme.colorScheme.surfaceContainerLow,
    )
    Card(modifier = modifier) {
        Column(
            modifier =
                Modifier
                    .animateContentSize()
                    .background(color = color),
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
            ) {
                NameCategory(
                    nameCategory = category.name,
                    modifier = Modifier.weight(1f),
                )
                ExpandItemButton(expanded = expanded, onClick = { expanded = !expanded })
            }
            if (expanded) {
                HorizontalDivider()
                ItemsListBody(
                    items = category.listItem,
                    itemStates = itemStates,
                    onItemCheckedChange = onItemCheckedChange,
                    onPlusQuantity = onPlusQuantity,
                    onSustractQuantity = onSustractQuantity,
                    modifier = Modifier.padding(4.dp),
                )
            }
        }
    }
}

@Composable
private fun ItemsListBody(
    items: List<ItemModel>,
    itemStates: Map<ItemModel, ItemState>,
    onItemCheckedChange: (ItemModel, Boolean) -> Unit,
    onPlusQuantity: (ItemModel) -> Unit,
    onSustractQuantity: (ItemModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier.heightIn(max = 200.dp)) {
        items(items.size) { index ->
            val item = items[index]
            ItemRow(
                item = item,
                itemState = itemStates[item] ?: ItemState(),
                onItemCheckedChange = onItemCheckedChange,
                onPlusQuantity = onPlusQuantity,
                onSustractQuantity = onSustractQuantity,
            )
        }
    }
}

@Composable
private fun ItemRow(
    item: ItemModel,
    itemState: ItemState,
    onItemCheckedChange: (ItemModel, Boolean) -> Unit,
    onPlusQuantity: (ItemModel) -> Unit,
    onSustractQuantity: (ItemModel) -> Unit,
    modifier: Modifier = Modifier,
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
            checked = itemState.isChecked,
            onCheckedChange = {
                onItemCheckedChange(item, it)
                if (itemState.quantity == 0) {
                    onPlusQuantity(item)
                }
            },
        )
        Text(item.name)
        Spacer(modifier = Modifier.weight(2f))
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = { onSustractQuantity(item) },
                enabled = itemState.quantity > 0,
            ) {
                Icon(Icons.Filled.Remove, contentDescription = "Disminuir cantidad")
            }
            Text(text = itemState.quantity.toString())
            IconButton(onClick = { onPlusQuantity(item) }) {
                Icon(Icons.Filled.Add, contentDescription = "Aumentar cantidad")
            }
        }
    }
}

@Composable
private fun NameCategory(
    nameCategory: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = nameCategory,
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.primary,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier,
    )
}

@Composable
private fun ExpandItemButton(
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Icon(
            imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
        )
    }
}

private fun isItemChecked(
    item: ItemModel,
    checkedStates: MutableMap<ItemModel, Boolean>,
) = checkedStates[item] ?: false

private fun isItemQuantityPositive(
    item: ItemModel,
    quantityStates: Map<ItemModel, Int>,
) = (quantityStates[item] ?: 0) > 0
/*
@Preview(showBackground = true)
@Composable
private fun AddItemScreenPreview() {
    AppTheme {
        AddItemsBody(
            categoryList = emptyList(),
            // Mapa vacío
            checkedStates = remember { mutableStateOf(mutableMapOf()) },
            // Función vacía
            onItemCheckedChange = { _, _ , _ ,-> },
        )
    }
}
*/
