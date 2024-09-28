package ar.edu.unlam.mobile.scaffolding.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import ar.edu.unlam.mobile.scaffolding.NameappTopAppBar
import ar.edu.unlam.mobile.scaffolding.R
import ar.edu.unlam.mobile.scaffolding.ui.navigation.NavigationDestination
import com.example.compose.AppTheme

object AddItemsToShoppingListDestination : NavigationDestination {
    override val route = "addItemsToShoppingList"
    override val titleRes = R.string.shopping_list_add_title

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemsToShoppingListScreen(
    navigateBack: () -> Unit,
    navigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: AddItemsToShoppingListViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            NameappTopAppBar(
                title = stringResource(AddItemsToShoppingListDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = navigateUp
            )
        }
    ) { innerPadding ->
        AddItemsBody(
            onSaveClick = {
                //viewModel.saveItem()
                navigateBack()
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )

    }

}

@Composable
fun AddItemsBody(
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column() {
        LazyColumn {
            items(10) {
                Text(text = "Item $it")

            }
        }
        Button(
            onClick = onSaveClick,
            enabled = true,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.save_action))
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun AddItemScreenPreview() {
    AppTheme {
        AddItemsBody(onSaveClick = {})
    }
}