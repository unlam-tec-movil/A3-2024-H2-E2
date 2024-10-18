package ar.edu.unlam.mobile.scaffolding.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ar.edu.unlam.mobile.scaffolding.ui.navigation.AppScreens
import ar.edu.unlam.mobile.scaffolding.ui.viewmodels.HomeViewModel

data class CardItem(
    val title: String,
    val cantidadProductos: Int,
    val profileImagesShared: List<Painter>?,
    val selectedColor: Color,
    val selectedIcon: ImageVector
)

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    // Obtenemos el estado de la lista de elementos.
    val listItems by viewModel.listItems.collectAsState()

    // Obtenemos el estado general (puedes usarlo si es necesario).
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = modifier.padding(16.dp)) {
        Text("Mis Listas",)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(listItems) { item ->
                CardInfo(
                    title = item.title,
                    cant = item.cantidadProductos,
                    navController = navController,
                    profileImagesShared = item.profileImagesShared,
                    color = item.selectedColor,
                    icon = item.selectedIcon
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
    profileImagesShared: List<Painter>?,
    color: Color,
    icon: ImageVector,
) {
    Card(
        modifier =
        Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clickable { navController.navigate(AppScreens.ShoppingList.route) },
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        // Usar un Row para acomodar el contenido y el ícono
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween // Espacia entre el contenido y el ícono
        ) {
            // Columna para el contenido principal
            Column(
                modifier = Modifier.weight(1f), // Ocupa el espacio disponible
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    maxLines = 1,
                    fontSize = 30.sp,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier =
                    Modifier
                        .background(
                            color = Color(0xFFFFA726),
                            shape = RoundedCornerShape(25.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
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
                        verticalAlignment = Alignment.CenterVertically
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
                        IconButton(onClick = { /* Acción de compartir */ }) {
                            Icon(
                                imageVector = Icons.Filled.Share,
                                contentDescription = "Compartir",
                                tint = Color.Blue
                            )
                        }
                    }
                }
            }

            // Ícono a la derecha
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp), // Ajusta el tamaño del ícono
                tint = Color.Black // Ajusta el color del ícono según sea necesario
            )
        }
    }
}
