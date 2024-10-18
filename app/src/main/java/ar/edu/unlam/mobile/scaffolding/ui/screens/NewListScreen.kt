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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ar.edu.unlam.mobile.scaffolding.ui.components.ColorPicker
import ar.edu.unlam.mobile.scaffolding.ui.components.IconPicker
import ar.edu.unlam.mobile.scaffolding.ui.viewmodels.HomeViewModel

@Composable
fun NewListScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavController,
) {
    var listName by remember { mutableStateOf("") }
    var selectedIcon by remember { mutableStateOf<ImageVector?>(null) }
    var selectedColor by remember { mutableStateOf<Color?>(null) }

    val icons =
        listOf(
            Icons.Default.Favorite,
            Icons.Default.Home,
            Icons.Default.Star,
            Icons.Default.ShoppingCart,
            Icons.Default.Person,
        )

    val colors =
        listOf(
            Color.Red,
            Color.Green,
            Color.Blue,
            Color.Yellow,
            Color(0xFFFFA500),
        )

//    Spacer(modifier = Modifier.height(50.dp))
    Log.i("ACA", "ACAAAA")
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(16.dp),
    ) {
        Text(text = "Crear una nueva lista")

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = listName,
            onValueChange = { listName = it },
            label = { Text("Nombre de la lista") },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Seleccionar un ícono")
        Spacer(modifier = Modifier.height(8.dp))

        ColorPicker(colors, selectedColor) { color ->
            selectedColor = color
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Seleccionar un color")
        Spacer(modifier = Modifier.height(8.dp))


        IconPicker(icons, selectedIcon) { icon ->
            selectedIcon = icon
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { if (listName.isNotEmpty()) {
            selectedIcon?.let { selectedColor?.let { it1 ->
                viewModel.addNewList(listName,
                    it1, it)
            } } // Agregar lista al ViewModel
//            Toast.makeText(LocalContext.current, "Lista creada", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        } }) {
            Text("Crear lista")
        }
    }
}
