package ar.edu.unlam.mobile.scaffolding.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ar.edu.unlam.mobile.scaffolding.R
import ar.edu.unlam.mobile.scaffolding.ui.navigation.NavigationDestination
import ar.edu.unlam.mobile.scaffolding.ui.viewmodels.NewListViewModel

object NewListDestination : NavigationDestination {
    override val route = "newList"
    override val titleRes = R.string.crear_nueva_lista
}

@Composable
fun NewListScreen(
    modifier: Modifier = Modifier,
    viewModel: NewListViewModel = hiltViewModel(),
    navController: NavController,
) {
    val newListState by viewModel.newListState.collectAsState()

    val imageResources =
        listOf(
            R.drawable.image0,
//            R.drawable.image1,
            R.drawable.image2,
//            R.drawable.image3,
            R.drawable.image4,
            R.drawable.image5,
            R.drawable.image6,
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

    //  animación de desvanecimiento al Column__
    Column(
        modifier =
            modifier
                .fillMaxSize()
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

        // Grid para seleccionar imágenes
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // 2 columnas
            modifier = Modifier.fillMaxWidth().height(400.dp),
            contentPadding = PaddingValues(8.dp),
        ) {
            items(imageResources.size) { index ->
                val imageRes = imageResources[index]
                val isSelected = newListState.selectedImage == imageRes
                Card(
                    modifier =
                        Modifier
                            .padding(8.dp)
                            .clickable {
                                viewModel.updateSelectedImage(imageRes)
                            }.border(
                                width = 2.dp,
                                color = if (isSelected) Color.Blue else Color.Transparent,
                                shape = RoundedCornerShape(8.dp),
                            ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                ) {
                    val options =
                        BitmapFactory.Options().apply {
                            inSampleSize = 16 // Escala la imagen a 1/4 del tamaño original
                        }

                    val bitmap = BitmapFactory.decodeResource(LocalContext.current.resources, imageRes, options)

                    LoadImage(bitmap)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.createNewList()
                navController.popBackStack()
            },
            enabled = viewModel.isFormValid(),
        ) {
            Text("Crear lista")
        }
    }
}

@Composable
fun LoadImage(bitmap: Bitmap) {
//    val painter = painterResource(id = imageRes)

    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = "Imagen seleccionable",
        modifier =
            Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
        contentScale = ContentScale.Crop,
    )
}
