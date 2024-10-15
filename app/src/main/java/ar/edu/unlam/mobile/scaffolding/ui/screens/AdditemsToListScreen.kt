package ar.edu.unlam.mobile.scaffolding.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ar.edu.unlam.mobile.scaffolding.data.local.DataSource
import ar.edu.unlam.mobile.scaffolding.ui.theme.AppTheme

@Composable
fun AddItemsToShoppingListScreen(
    modifier: Modifier = Modifier,
    viewModel: AddItemsToShoppingListViewModel = hiltViewModel(),
) {
    val checkedStates = viewModel.checkedStates.collectAsState()

    AddItemsBody(
        onSaveClick = {
            // viewModel.saveItem()
        },
        categoryList = DataSource.categoryList,
        modifier =
            modifier
                .fillMaxWidth(),
        checkedStates = checkedStates.value,
        onItemCheckedChange = { item, isChecked -> viewModel.onItemCheckedChange(item, isChecked) },
    )
}

@Composable
fun AddItemsBody(
    onSaveClick: () -> Unit,
    categoryList: List<Category>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    checkedStates: MutableMap<Item, Boolean>,
    onItemCheckedChange: (Item, Boolean) -> Unit,
) {
    Column(modifier = modifier) {
        LazyColumn {
            items(categoryList.size) {
                CategoryItem(
                    category = categoryList[it],
                    modifier = Modifier.padding(8.dp),
                    checkedStates = checkedStates,
                    onItemCheckedChange = onItemCheckedChange,
                )
            }
        }
    }
}

@Composable
fun CategoryItem(
    category: Category,
    modifier: Modifier,
    // Agrega checkedStates
    checkedStates: Map<Item, Boolean>,
    // Agrega onItemCheckedChange
    onItemCheckedChange: (Item, Boolean) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val color by animateColorAsState(
        targetValue = if (expanded) MaterialTheme.colorScheme.surfaceContainerHighest else MaterialTheme.colorScheme.surfaceContainerLow,
    )
    Card(modifier = modifier) {
        Column(
            modifier =
                Modifier
                    .animateContentSize(
                        animationSpec =
                            spring(
                                dampingRatio = Spring.DampingRatioNoBouncy,
                                stiffness = Spring.StiffnessMedium,
                            ),
                    ).background(color = color),
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
            ) {
                NameCategory(
                    nameCategory = category.nameCategory,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .weight(1f),
                )

                // Spacer(modifier = Modifier.weight(1f))
                ExpandItemButton(expanded = expanded, onClick = { expanded = !expanded })
            }
            if (expanded) {
                HorizontalDivider()
                ItemsListBody(
                    items = category.items,
                    modifier =
                        Modifier.padding(
                            4.dp,
                        ),
                    checkedStates = checkedStates,
                    onItemCheckedChange = onItemCheckedChange,
                )
            }
        }
    }
}

@Composable
private fun ItemsListBody(
    items: List<Item>,
    modifier: Modifier = Modifier,
    // Agrega checkedStates
    checkedStates: Map<Item, Boolean>,
    // Agrega onItemCheckedChange
    onItemCheckedChange: (Item, Boolean) -> Unit,
) {
    LazyColumn(modifier = modifier.heightIn(max = 200.dp)) {
        items(items.size) {
            ItemRow(
                item = items[it],
                checkedStates = checkedStates,
                onItemCheckedChange = onItemCheckedChange,
            )
        }
    }
}

@Composable
fun ItemRow(
    item: Item,
    modifier: Modifier = Modifier,
    checkedStates: Map<Item, Boolean>,
    onItemCheckedChange: (Item, Boolean) -> Unit,
) {
    val isChecked = checkedStates[item] ?: false

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = { onItemCheckedChange(item, it) },
            modifier = Modifier.padding(0.dp),
        )
        Text(item.name)
        Spacer(modifier = Modifier.weight(2f))
        // Text("Menor precio en la tienda")
        Spacer(modifier = Modifier.weight(0.5f))
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { item.quantity-- }) {
                Icon(Icons.Filled.Remove, contentDescription = "Disminuir cantidad")
            }
            Text(text = item.quantity.toString())
            IconButton(onClick = { item.quantity++ }) {
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

data class Category(
    val nameCategory: String,
    val items: List<Item>,
)

data class Item(
    val name: String,
    var quantity: Int = 0,
)

@Preview(showBackground = true)
@Composable
private fun AddItemScreenPreview() {
    AppTheme {
        AddItemsBody(
            onSaveClick = {},
            categoryList = DataSource.categoryList,
            // Mapa vacío
            checkedStates = mutableMapOf(),
            // Función vacía
            onItemCheckedChange = { _, _ -> },
        )
    }
}
