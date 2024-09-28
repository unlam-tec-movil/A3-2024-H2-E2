package ar.edu.unlam.mobile.scaffolding.ui.screens

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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ar.edu.unlam.mobile.scaffolding.R


data class CardItem(
    val title: String,
    val cantidadProductos: Int,
    val profileImagesShared: List<Painter>?
)

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    // La información que obtenemos desde el view model la consumimos a través de un estado de
    // "tres vías": Loading, Success y Error. Esto nos permite mostrar un estado de carga,
    // un estado de éxito y un mensaje de error.
    val uiState: HomeUIState by viewModel.uiState.collectAsState()

    val profileImages = listOf(
        painterResource(id = R.drawable.profile_pic1),
        painterResource(id = R.drawable.profile_pic1),
        painterResource(id = R.drawable.profile_pic1)
    )

    val cardItems = listOf(
        CardItem("Lista 1", 7, profileImages),
        CardItem("Lista 2", 5, profileImages),
        CardItem("Lista 3", 3, profileImages),
        CardItem("Lista 4", 1, null),
        CardItem("Lista 5", 12, null),
    )

    when (val helloState = uiState.helloMessageState) {
        is HelloMessageUIState.Loading -> {
            // Loading
        }

        is HelloMessageUIState.Success -> {
            Spacer(modifier = Modifier.height(500.dp))

//            Greeting(helloState.message, modifier)

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(top = 56.dp, bottom = 70.dp),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(cardItems) { item ->
                    CardInfo(
                        title = item.title,
                        cant = item.cantidadProductos,
                        profileImagesShared =  item.profileImagesShared
                    )
                }
            }

            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(bottom = 56.dp) // Añadir padding inferior para que no se solape con el BottomBar
            ) {
                FloatingActionButton(
                    onClick = { /* Acción al hacer clic */ },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    containerColor = Color(0xFFFFA726),
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Agregar Lista")
                }
            }
        }

        is HelloMessageUIState.Error -> {
            // Error
        }
    }
}

@Composable
fun CardInfo(title: String, cant: Int, profileImagesShared: List<Painter>?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clickable { /* No hacer nada por ahora */ },
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFA726) // el del material theme no me gusta xd
        ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(
                        text = title,
                        maxLines = 1,
                        fontSize = 30.sp,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .background(
                                color = Color(0xFFFFA726),
                                shape = RoundedCornerShape(25.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "$cant producto/s ",
                            maxLines = 2,
                        )
                    }

//                    Spacer(modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(end = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            profileImagesShared?.forEach { profileImage ->
                                Image(
                                    painter = profileImage,
                                    contentDescription = "Imagen de perfil",
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Color.Gray, CircleShape)
                                        .padding(end = 8.dp),
                                    contentScale = ContentScale.Crop
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
            }
        }
    }
}