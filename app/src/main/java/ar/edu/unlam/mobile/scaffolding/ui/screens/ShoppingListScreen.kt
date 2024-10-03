package ar.edu.unlam.mobile.scaffolding.ui.screens

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ar.edu.unlam.mobile.scaffolding.ui.theme.AppTheme

@Composable
fun ShoppingListScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: ShoppingListViewModel = hiltViewModel()
) {
    Column(modifier =modifier) {

        ShoppingListBody(
            itemsList = DataSource.products,
            onItemClick = { /*TODO acción para editar la cantidad o el item*/ },
            )

    }

}

@Composable
fun ShoppingListBody(
    itemsList: List<ItemProduct>,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        if (itemsList.isEmpty()) {
            Text(
                text = "Lista de compras vacía, agregue un producto",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(contentPadding),
            )
        }else{
            ShoppingListItems(itemsList = itemsList,)

        }
    }
}

@Composable
fun ShoppingListItems(itemsList: List<ItemProduct>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(itemsList) { item ->
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
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
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
            ShoppingListScreen(navController = NavController(LocalContext.current))
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

private object DataSource {
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