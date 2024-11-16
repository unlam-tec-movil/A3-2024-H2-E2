package ar.edu.unlam.mobile.scaffolding.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ar.edu.unlam.mobile.scaffolding.R
import ar.edu.unlam.mobile.scaffolding.ui.navigation.NavigationDestination

// path
object AddNewProdDestino : NavigationDestination {
    override val route = "newProd"
    override val titleRes = R.string.new_prod
}

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewProd(
    viewModel: AddProdNewViewModel = viewModel(),
    navController: NavController,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Limpieza") },
                colors =
                    TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color(0xFFFF9800), // Fondo naranja
                        titleContentColor = Color.White, // Texto blanco
                    ),
            )
        },
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(16.dp),
            verticalArrangement = Arrangement.Top,
        ) {
            Text("Escribí el nombre del ítem a agregar", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de texto para el nombre del ítem
            TextField(
                value = viewModel.itemName.value,
                onValueChange = { viewModel.itemName.value = it },
                label = { Text("Nombre del ítem") },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de texto para la cantidad
            TextField(
                value = viewModel.itemQuantity.value,
                onValueChange = { viewModel.itemQuantity.value = it },
                label = { Text("Cantidad") },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para agregar
            Button(
                onClick = {
                    // Lógica para manejar el evento de agregar
                    viewModel.clearInputs()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
            ) {
                Text("Agregar", color = Color.White)
            }
        }
    }
}
