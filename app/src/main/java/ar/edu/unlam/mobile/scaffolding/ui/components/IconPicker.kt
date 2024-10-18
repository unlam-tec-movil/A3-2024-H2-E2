package ar.edu.unlam.mobile.scaffolding.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun IconPicker(
    icons: List<ImageVector>,
    selectedIcon: ImageVector?,
    onIconSelected: (ImageVector) -> Unit,
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(vertical = 16.dp)
    ) {
        items(icons) { icon ->
            val isSelected = selectedIcon == icon
            IconButton(
                onClick = { onIconSelected(icon) },
                modifier = Modifier
                    .size(40.dp)
                    .border(
                        width = if (isSelected) 2.dp else 0.dp,
                        color = if (isSelected) Color.Blue else Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.Black
                )
            }
        }
    }
}
