package ar.edu.unlam.mobile.scaffolding.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import ar.edu.unlam.mobile.scaffolding.NameappTopAppBar
import ar.edu.unlam.mobile.scaffolding.R
import ar.edu.unlam.mobile.scaffolding.ui.components.BottomBar
import ar.edu.unlam.mobile.scaffolding.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch


object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToAddShoppingList: () -> Unit,
    navigateToItemUpdate: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    // La información que obtenemos desde el view model la consumimos a través de un estado de
    // "tres vías": Loading, Success y Error. Esto nos permite mostrar un estado de carga,
    // un estado de éxito y un mensaje de error.
    val uiState: HomeUIState by viewModel.uiState.collectAsState()

    val controller = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val menuItems = listOf("Home", "Perfil", "Configuración", "Cerrar sesión")

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            // Contenido del menú hamburguesa
            Column(modifier = Modifier.fillMaxSize().background(Color(0xFFFFA726))) {
                Text(
                    text = "Menú",
                    modifier = Modifier.padding(16.dp),
                )
                Divider()
                menuItems.forEach { item ->
                    TextButton(
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            // Aquí puedes agregar acciones según el item seleccionado
                            when (item) {
                                "Home" -> controller.navigate("home")
                                "Perfil" -> controller.navigate("profile")
                                "Configuración" -> controller.navigate("settings")
                                "Cerrar sesión" -> {/* Acción de cerrar sesión */
                                }
                            }
                        }
                    ) {
                        Text(text = item, modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
    ) {
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            NameappTopAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = { BottomBar(controller = controller) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToAddShoppingList,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.shopping_list_add_title)
                )
            }
        },
    ) { innerPadding ->
        HomeBody(
            allShoppingList = cardItems.cardItems,
            onItemClick = navigateToItemUpdate,
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = innerPadding,
        )
    }

    }


    /* when (val helloState = uiState.helloMessageState) {
         is HelloMessageUIState.Loading -> {
             // Loading
         }

         is HelloMessageUIState.Success -> {
             Spacer(modifier = Modifier.height(500.dp))

 //            Greeting(helloState.message, modifier)


         }

         is HelloMessageUIState.Error -> {
             // Error
         }
     }*/
}

@Composable
private fun HomeBody(
    allShoppingList: List<CardItem>,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        if (allShoppingList.isEmpty()) {
            Text(
                text = "No hay elementos para mostrar",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        } else {
            AllShoppingList(
                AllShoppingList = allShoppingList,
                onItemClick = onItemClick,
                contentPadding = contentPadding,
                modifier = modifier
            )

        }
    }
}

@Composable
private fun AllShoppingList(
    AllShoppingList: List<CardItem>,
    onItemClick: (Int) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier

) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 56.dp),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(AllShoppingList) { item ->
            CardInfo(
                title = item.title,
                description = item.description,
                icon = item.icon
            )
        }
    }

}

@Composable
fun CardInfo(title: String, description: String, icon: ImageVector) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp), // Ajustamos la altura para tener un diseño consistente
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    icon,
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 16.dp)
                )
                Column {
                    Text(
                        text = title,
                        maxLines = 1,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = description,
                        maxLines = 2,
                    )
                }
            }
        }
    }
}

data class CardItem(val title: String, val description: String, val icon: ImageVector)

object cardItems {
    val cardItems = listOf(
        CardItem("Lista 1", "Este es el detalle de la notificación 1", Icons.Default.Info),
        CardItem("Lista 2", "Este es el detalle de la notificación 2", Icons.Default.Info),
        CardItem("Lista 3", "Este es el detalle de la notificación 3", Icons.Default.Info),
        CardItem("Lista 4", "Este es el detalle de la notificación 4", Icons.Default.Info),
        CardItem("Lista 5", "Este es el detalle de la notificación 5", Icons.Default.Info),
    )


}