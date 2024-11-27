package ar.edu.unlam.mobile.scaffolding.ui.components

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun UbicacionHandler(
    permission: String,
    rationaleMessage: String,
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit,
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(true) }

    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
        ) { isGranted ->
            showDialog = false // Ocultar el diálogo tras conceder o denegar el permiso
            if (isGranted) {
                onPermissionGranted()
            } else {
                onPermissionDenied()
            }
        }

    // Comprobar si el permiso ya está concedido
    val hasPermission =
        ContextCompat.checkSelfPermission(
            context,
            permission,
        ) == PackageManager.PERMISSION_GRANTED

    if (hasPermission) {
        onPermissionGranted()
    } else if (showDialog) {
        // Mostrar un diálogo para explicar por qué se necesita el permiso
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Permiso requerido") },
            text = { Text(rationaleMessage) },
            confirmButton = {
                Button(onClick = {
                    showDialog = false // Ocultar el diálogo
                    permissionLauncher.launch(permission)
                }) {
                    Text("Conceder permiso")
                }
            },
            dismissButton = {
                Button(onClick = {
                    showDialog = false
                    onPermissionDenied()
                }) {
                    Text("Cancelar")
                }
            },
        )
    }
}
