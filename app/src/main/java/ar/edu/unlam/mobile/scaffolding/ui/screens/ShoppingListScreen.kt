package ar.edu.unlam.mobile.scaffolding.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ar.edu.unlam.mobile.scaffolding.ShopListTopAppBar
import ar.edu.unlam.mobile.scaffolding.data.local.shoppinglist.ItemWithQuantityAndChecked
import ar.edu.unlam.mobile.scaffolding.ui.DetectorMovimiento
import ar.edu.unlam.mobile.scaffolding.ui.components.CameraHandler
import ar.edu.unlam.mobile.scaffolding.ui.navigation.NavigationDestination

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
    navigateToAddItem: (Long) -> Unit,
) {
    val listId =
        navController.currentBackStackEntry?.arguments?.getLong(ShoppingListDestination.LIST_ID_ARG)
    navController.currentBackStackEntry?.savedStateHandle?.set(
        ShoppingListDestination.LIST_ID_ARG,
        listId,
    )

    var expandedItemId by remember { mutableStateOf<Long?>(null) }

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

    Scaffold(
        topBar = {
            ShopListTopAppBar(
                title = "Lista de compras",
                canNavigateBack = true,
                navigateUp = { navController.navigateUp() },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToAddItem(listId ?: 0) },
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add items to list")
            }
        },
    ) { innerPadding ->

        when (uiState) {
            is ShoppingListUIState.Loading -> LoadingScreen()

            is ShoppingListUIState.Error -> ErrorMessage(message = "Error al cargar listas")

            is ShoppingListUIState.Success -> {
                val itemsList = (uiState as ShoppingListUIState.Success).itemLists
                ShoppingListBody(
                    itemsList = itemsList,
                    viewModel = viewModel,
                    expandedItemId = expandedItemId,
                    modifier =
                        modifier
                            .fillMaxSize()
                            .alpha(alpha)
                            .padding(innerPadding),
                    onItemExpanded = { id ->
                        expandedItemId = if (expandedItemId == id) null else id
                    },
                )
            }
        }
    }
}

@Composable
fun ShoppingListBody(
    itemsList: List<ItemWithQuantityAndChecked>,
    viewModel: ShoppingListViewModel,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    expandedItemId: Long?,
    onItemExpanded: (Long) -> Unit,
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
            ShoppingListItems(
                itemsList = itemsList,
                viewModel = viewModel,
                expandedItemId = expandedItemId,
                onItemExpanded = onItemExpanded,
            )
        }
    }
}

@Composable
fun ShoppingListItems(
    itemsList: List<ItemWithQuantityAndChecked>,
    viewModel: ShoppingListViewModel,
    modifier: Modifier = Modifier,
    expandedItemId: Long?,
    onItemExpanded: (Long) -> Unit,
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
                updatePhoto = { id, bitmap ->
                    viewModel.saveImage(id, bitmap)
                },
                isExpanded = item.id == expandedItemId,
                expandedItemId = expandedItemId,
                onExpand = { onItemExpanded(item.id) },
            )
        }
    }
}

@Composable
fun ItemRow(
    item: ItemWithQuantityAndChecked,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    updatePhoto: (Long, Bitmap) -> Unit,
    isExpanded: Boolean,
    expandedItemId: Long?,
    onExpand: () -> Unit,
) {
    val context = LocalContext.current

    // Manejar la cámara
    val abrirCamera =
        CameraHandler(
            onImageCaptured = { bitmap ->
                if (bitmap != null) {
                    updatePhoto(item.id, bitmap)
                    Toast
                        .makeText(context, "Foto capturada correctamente", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(context, "No se capturó imagen", Toast.LENGTH_SHORT).show()
                }
            },
            onPermissionDenied = {
                Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
            },
        )

    Log.i("expanded item2", expandedItemId.toString())
    DisposableEffect(expandedItemId, isExpanded) {
        var cameraOpened = false
        val shakeDetector =
            DetectorMovimiento(context) {
                // Solo abre la cámara si el ítem está expandido y es el ítem seleccionado
                Log.i("expanded item", expandedItemId.toString())
                if (isExpanded && expandedItemId == item.id && !cameraOpened) {
                    abrirCamera()
                    cameraOpened = true
                }
            }

        shakeDetector.start()

        // Limpieza del detector cuando el Composable cambien los parámetros
        onDispose {
            shakeDetector.stop()
        }
    }

    Card(
        modifier = modifier.clickable { onExpand() },
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Row con los datos principales del ítem
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Checkbox(
                    checked = item.isChecked,
                    onCheckedChange = { isChecked -> onCheckedChange(isChecked) },
                )
                Text(
                    text = item.name,
                    textDecoration =
                        if (item.isChecked) {
                            TextDecoration.LineThrough
                        } else {
                            null
                        },
                    modifier = Modifier.weight(1f),
                )

                Text(
                    text = "x${item.quantity}",
                    modifier = Modifier.padding(start = 8.dp),
                )

                IconButton(
                    onClick = { abrirCamera() },
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Tomar foto",
                    )
                }
            }

            AnimatedVisibility(
                visible = expandedItemId == item.id,
                enter = expandVertically(animationSpec = tween(durationMillis = 300)) + fadeIn(),
                exit = shrinkVertically(animationSpec = tween(durationMillis = 300)) + fadeOut(),
            ) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    item.photo?.let { photo ->
                        val bitmap =
                            remember(photo) {
                                Base64.decode(photo, Base64.DEFAULT).let { byteArray ->
                                    BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                                }
                            }
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Foto del ítem",
                            modifier =
                            Modifier
                                .width(150.dp)
                                .height(150.dp)
                                .clip(MaterialTheme.shapes.medium),
                            contentScale = ContentScale.Crop,
                        )
                    } ?: Text(
                        "No hay imagen disponible",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}
