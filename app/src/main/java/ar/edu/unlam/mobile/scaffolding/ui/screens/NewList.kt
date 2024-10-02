package ar.edu.unlam.mobile.scaffolding.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun NewListScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val icons = listOf(
        Icons.Default.Favorite,
        Icons.Default.Home,
        Icons.Default.Star,
        Icons.Default.ShoppingCart,
        Icons.Default.Person
    )

    val colors = listOf(
        Color.Red, Color.Green, Color.Blue, Color.Yellow, Color(0xFFFFA500) // Naranja
    )

//    Spacer(modifier = Modifier.height(50.dp))
    Log.i("ACA", "ACAAAA")
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Crear una nueva lista")

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Nombre de la lista") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Seleccionar un ícono")
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            icons.forEach { icon ->
                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background( /*if (selectedIcon == icon) Color.LightGray else*/ Color.Transparent)
                        .border(
                            width = 2.dp,
                            color = /*if (selectedIcon == icon) Color(0xFFFFA500) else */Color.Black,
                            shape = CircleShape
                        )
                ) {
                    Icon(icon, contentDescription = null, tint = Color.Black)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Seleccionar un color")
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            colors.forEach { color ->
                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(color)
                        .border(
                            width = 2.dp,
                            color = /*if (selectedColor == color) Color.Black else*/ Color.Transparent,
                            shape = CircleShape
                        )
                ) {
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { /* guarda la lista */ }) {
            Text("Crear lista")
        }
    }
}