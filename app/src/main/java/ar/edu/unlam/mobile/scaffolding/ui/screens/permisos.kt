package ar.edu.unlam.mobile.scaffolding.ui.screens

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestLocationPermissionDialog(
    permissionState: PermissionState,
    onPermissionRequest: () -> Unit,
) {
    if (!permissionState.status.isGranted) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text(text = "Permiso de ubicación requerido") },
            text = {
                Text(text = "Esta aplicación necesita acceso a tu ubicación para mostrar los supermercados cercanos.")
            },
            confirmButton = {
                Button(onClick = onPermissionRequest) {
                    Text("Conceder permiso")
                }
            },
            dismissButton = {
                Button(onClick = { /* Acción opcional */ }) {
                    Text("Cancelar")
                }
            },
        )
    }
}
