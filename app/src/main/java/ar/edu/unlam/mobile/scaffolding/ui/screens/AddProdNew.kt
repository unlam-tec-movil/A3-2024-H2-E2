package ar.edu.unlam.mobile.scaffolding.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ar.edu.unlam.mobile.scaffolding.R

@Composable
fun AddProdNew(viewModel: AddProdNewViewModel = viewModel()) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_launcher_background), // ESTO HAY QUE CAMBIARLO POR EL ICONO QUE VA
                            contentDescription = "Cart",
                            tint = Color.Black,
                            modifier = Modifier.size(24.dp),
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Limpieza", // esto se reemplazar con la informacion porvista por la bd
                            color = Color.Black,
                            fontSize = 20.sp,
                        )
                    }
                },
                backgroundColor = Color(0xFFFFF2E0),
                elevation = 4.dp,
            )
        },
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            Content(viewModel)
        }
    }
}

@Composable
fun Content(viewModel: AddProdNewViewModel) {
    val itemName = viewModel.prodName.observeAsState("")
    val itemCantidad = viewModel.prodCantidad.observeAsState("")

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Card(
            shape = RoundedCornerShape(8.dp),
            elevation = 4.dp,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Escribí el nombre del ítem a agregar",
                    color = Color(0xFFFF6600),
                    fontSize = 16.sp,
                )
                Spacer(modifier = Modifier.height(16.dp))
                CustomTextField(
                    value = itemName.value,
                    onValueChange = { viewModel.onItemNameChange(it) },
                    label = "Jabón líquido",
                    icon = R.drawable.ic_launcher_background, // se reemplaza cn el icono que va
                )
                Spacer(modifier = Modifier.height(16.dp))
                CustomTextField(
                    value = itemCantidad.value,
                    onValueChange = { viewModel.onItemQuantityChange(it) },
                    label = "Ingresa la cantidad",
                    icon = R.drawable.ic_launcher_background, // se reemplaza con el icono que va
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                // Lógica para agregar el ítem
                viewModel.clearFields()
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFF6600)),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(50.dp),
        ) {
            Text(
                text = "Agregar",
                color = Color.White,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: Int,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label, color = Color(0xFFFF6600)) },
        singleLine = true,
        trailingIcon = {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "Icon",
                tint = Color(0xFFFF6600),
            )
        },
        colors =
            TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFFFF6600),
                unfocusedBorderColor = Color(0xFFFF6600),
                textColor = Color.Black,
            ),
        modifier = Modifier.fillMaxWidth(),
    )
}
