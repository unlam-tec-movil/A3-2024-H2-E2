package ar.edu.unlam.mobile.scaffolding.ui.components

import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

@Composable
fun ShareButton(itemsToShare: List<String>) {
    val context = LocalContext.current
    val shareContent = itemsToShare.joinToString(separator = "\n") { it }

    IconButton(onClick = {
        val sendIntent =
            Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareContent)
                type = "text/plain" //  de contenido
            }

        val chooser = Intent.createChooser(sendIntent, "Compartir con")
        context.startActivity(chooser) // Lanzar el Intent
    }) {
        Icon(
            imageVector = Icons.Filled.Share,
            contentDescription = "Compartir",
            tint = Color.Blue,
        )
    }
}
