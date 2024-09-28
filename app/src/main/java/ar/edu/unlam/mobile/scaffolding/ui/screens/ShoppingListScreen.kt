package ar.edu.unlam.mobile.scaffolding.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ar.edu.unlam.mobile.scaffolding.R
import ar.edu.unlam.mobile.scaffolding.ui.navigation.NavigationDestination
import com.example.compose.AppTheme

object ShoppingListDestination : NavigationDestination {
    override val route = "shopping_list"
    override val titleRes = R.string.shopping_list_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(
    navigateBack: () -> Unit = {},
    onNavigateUp: () -> Unit = {},
    navigateToAddItems: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: ShoppingListViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
topBar = {
    TopAppBar(
        title = { Text("Nombre de Lista") },

        scrollBehavior = scrollBehavior
    )
},
        floatingActionButton = {
            FloatingActionButton(onClick = navigateToAddItems) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar producto")
            }
        }
    ) { innerPadding ->
        ShoppingListItems(DataSource.products, modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun ShoppingListItems(itemsProduct: List<ItemProduct>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(itemsProduct) { item ->
            ItemRow(
                item,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun ItemRow(item: ItemProduct, modifier: Modifier = Modifier) {
    var quantity by remember { mutableStateOf(item.quantity) }
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Checkbox(checked = false, onCheckedChange = {}, modifier = Modifier.padding(0.dp))
            Text(item.name)
            Spacer(modifier = Modifier.weight(2f))
            /*Text("Menor precio en la tienda")*/
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
           ShoppingListScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemPreview() {
    ItemRow(
        ItemProduct(name = "detergente", 2)
    )
}

@Preview(showBackground = true)
@Composable
fun ShoppingListPreview() {
    ShoppingListItems(DataSource.products)
}

object DataSource {
    val products = listOf(
        ItemProduct(name = "Detergente", 2),
        ItemProduct(name = "Jabón Liquido", 3),
        ItemProduct(name = "Desodorante", 5),
        ItemProduct(name = "Lustra muebles", 1),
    )
}

data class ItemProduct(
    val name: String,
    val quantity: Int,

    )