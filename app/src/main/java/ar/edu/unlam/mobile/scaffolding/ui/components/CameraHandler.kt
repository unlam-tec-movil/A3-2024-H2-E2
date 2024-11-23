package ar.edu.unlam.mobile.scaffolding.ui.components

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun CameraHandler(
    onImageCaptured: (Bitmap?) -> Unit,
    onPermissionDenied: () -> Unit,
): () -> Unit {
    val context = LocalContext.current

    //  capturar imagen
    val cameraLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicturePreview(),
        ) { bitmap ->
            onImageCaptured(bitmap)
        }

    //  solicitar permisos
    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
        ) { isGranted ->
            if (isGranted) {
                cameraLauncher.launch(null)
            } else {
                onPermissionDenied()
            }
        }

    // Devuelve una función para abrir la cámara con permisos
    return {
        val permission = Manifest.permission.CAMERA
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            cameraLauncher.launch(null)
        } else {
            permissionLauncher.launch(permission)
        }
    }
}
