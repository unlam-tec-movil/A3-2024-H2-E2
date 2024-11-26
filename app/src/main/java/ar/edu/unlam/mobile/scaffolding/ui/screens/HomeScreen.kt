package ar.edu.unlam.mobile.scaffolding.ui.screens

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ar.edu.unlam.mobile.scaffolding.R
import ar.edu.unlam.mobile.scaffolding.ShopListTopAppBar
import ar.edu.unlam.mobile.scaffolding.data.local.shoppinglist.ItemWithQuantityAndChecked
import ar.edu.unlam.mobile.scaffolding.domain.shoppinglist.ShoppingListModel
import ar.edu.unlam.mobile.scaffolding.ui.navigation.NavigationDestination
import ar.edu.unlam.mobile.scaffolding.ui.viewmodels.HomeUIState
import ar.edu.unlam.mobile.scaffolding.ui.viewmodels.HomeViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.mis_listas
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigateToList: (Long) -> Unit,
    navigateToNewList: () -> Unit,
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    val shoppingListItems by viewModel.shoppingListItems.observeAsState(emptyList())
    val context = LocalContext.current
    val loadShoppingListItems: (Long, String) -> Unit = { listId: Long, name: String ->
        viewModel.loadShoppingListItems(listId) {
            shareListItems(shoppingListItems, name, context)
        }
    }
    val controller = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                drawerState = drawerState,
                controller = controller,
                coroutineScope = coroutineScope,
            )
        },
    ) {
        Scaffold(
            topBar = {
                ShopListTopAppBar(
                    title = stringResource(HomeDestination.titleRes),
                    canNavigateBack = false,
                    onOpenDrawer = {
                        coroutineScope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    },
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = navigateToNewList,
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add items to list")
                }
            },
            bottomBar = {
            },
        ) { innerPadding ->
            when (uiState) {
                is HomeUIState.Loading -> LoadingScreen()

                is HomeUIState.Error -> ErrorMessage(message = "Error al cargar listas")

                is HomeUIState.Success -> {
                    val successState = uiState as HomeUIState.Success
                    HomeScreenBody(
                        shoppingLists = successState.shoppingLists,
                        isRefreshing = successState.isRefreshing,
                        onRefresh = viewModel::refreshShoppingLists,
                        navigateToList = navigateToList,
                        navController = navController,
                        modifier = modifier,
                        loadShoppingListItems = loadShoppingListItems,
                        contentPadding = innerPadding,
                    )
                }
            }
        }
    }
}

@Composable
fun DrawerContent(
    drawerState: DrawerState,
    controller: NavController,
    coroutineScope: CoroutineScope,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primaryContainer),
    ) {
        Text(
            text = "Menú",
            modifier = Modifier.padding(16.dp),
        )
        HorizontalDivider()

        NavigationDrawerItem(
            icon = {
                Icon(Icons.Filled.PinDrop, contentDescription = "Home")
            },
            label = {
                Text(text = "Mapa")
            },
            selected = true,
            onClick = {
                coroutineScope.launch { drawerState.close() }
                controller.navigate(MapScreenDestination.route)
            },
        )

        NavigationDrawerItem(
            label = {
                Text(text = "Listas Archivadas")
            },
            selected = false,
            onClick = { },
        )

        NavigationDrawerItem(
            label = {
                Text(text = "Comparador")
            },
            selected = false,
            onClick = { },
        )
    }
}

@Composable
fun HomeScreenBody(
    shoppingLists: List<ShoppingListModel>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    navigateToList: (Long) -> Unit,
    navController: NavController,
    modifier: Modifier,
    loadShoppingListItems: (Long, String) -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Column(modifier = modifier.padding(16.dp)) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
            onRefresh = onRefresh,
        ) {
            if (shoppingLists.isEmpty()) {
                EmptyListMessage()
            } else {
                ShoppingListContent(
                    shoppingLists = shoppingLists,
                    navigateToList = navigateToList,
                    navController = navController,
                    loadShoppingListItems = loadShoppingListItems,
                    contentPadding = contentPadding,
                )
            }
        }
    }
}

@Composable
fun ShoppingListContent(
    shoppingLists: List<ShoppingListModel>,
    navigateToList: (Long) -> Unit,
    navController: NavController,
    loadShoppingListItems: (Long, String) -> Unit,
    contentPadding: PaddingValues,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = contentPadding,
    ) {
        items(shoppingLists) { shoppingList ->
            stringToImageVector(shoppingList.selectedIcon)?.let { it ->
                CardInfo(
                    title = shoppingList.name,
                    cant = shoppingList.listItems.size,
                    navController = navController,
                    color = Color(shoppingList.selectedColor),
                    icon = it,
                    navigateToList = navigateToList,
                    listId = shoppingList.id?.toLong() ?: 0L,
                    loadShoppingListItems = loadShoppingListItems,
                )
            }
        }
    }
}

@Composable
fun CardInfo(
    title: String,
    cant: Int,
    navController: NavController,
    listId: Long,
    profileImagesShared: List<Painter>? = null,
    color: Color,
    icon: ImageVector,
    navigateToList: (Long) -> Unit,
    loadShoppingListItems: (Long, String) -> Unit,
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clickable {
                    navigateToList(listId)
                    Log.d("ListId", "listId en home: $listId")
            },
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = color),
    ) {
        // Usar un Row para acomodar el contenido y el ícono
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            // Columna para el contenido principal
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = title,
                    maxLines = 1,
                    fontSize = 30.sp,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier =
                        Modifier
                            .background(
                                color = Color(0xFFFFA726),
                                shape = RoundedCornerShape(25.dp),
                            ).padding(horizontal = 10.dp, vertical = 4.dp),
                ) {
                    Text(text = "$cant producto/s", maxLines = 2)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    // Mostrar imágenes de perfil
                    Row(
                        modifier = Modifier.padding(end = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        profileImagesShared?.forEach { profileImage ->
                            Image(
                                painter = profileImage,
                                contentDescription = "Imagen de perfil",
                                modifier =
                                    Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Color.Gray, CircleShape)
                                        .padding(end = 8.dp),
                                contentScale = ContentScale.Crop,
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                        Spacer(modifier = Modifier.width(4.dp))

                        IconButton(onClick = {
                            loadShoppingListItems(listId, title)
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Share,
                                contentDescription = "Compartir",
                                tint = Color.Blue,
                            )
                        }
                    }
                }
            }

            // Ícono a la derecha
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(50.dp),
                tint = Color.Black,
            )
        }
    }
}

@Composable
fun EmptyListMessage() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text("No tienes listas aún. ¡Crea una nueva!")
    }
}

@Composable
fun ErrorMessage(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Error en la carga",
            color = Color.Red,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

fun stringToImageVector(iconName: String): ImageVector? =
    when (iconName) {
        "Filled.Favorite" -> Icons.Filled.Favorite
        "Filled.Home" -> Icons.Filled.Home
        "Filled.Star" -> Icons.Filled.Star
        "Filled.ShoppingCart" -> Icons.Filled.ShoppingCart
        "Filled.Person" -> Icons.Filled.Person
        else -> Icons.Filled.ShoppingCart
    }

fun shareListItems(
    items: List<ItemWithQuantityAndChecked>,
    name: String,
    context: Context,
) {
    val shareText =
        buildString {
            append("Lista: $name\n\n") // Agrega el nombre de la lista al inicio
            append(
                items.joinToString(separator = "\n") { item ->
                    "- ${item.name}: ${item.quantity} ${if (item.isChecked) "(✓)" else "(✗)"}"
                },
            )
        }

    Log.i("ENVIO", shareText)
    // Usa Intent para compartir
    val shareIntent =
        Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
    context.startActivity(Intent.createChooser(shareIntent, "Compartir lista"))
}
