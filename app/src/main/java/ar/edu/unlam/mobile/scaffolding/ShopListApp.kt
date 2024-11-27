package ar.edu.unlam.mobile.scaffolding

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ar.edu.unlam.mobile.scaffolding.ui.navigation.AppNavHost

@Composable
fun ShopListApp2(navController: NavHostController = rememberNavController()) {
    AppNavHost(navController = navController)
}

/**
 * App bar to display title and conditionally display the back navigation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopListTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateUp: () -> Unit = {},
    onOpenDrawer: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFFFFA500),
                titleContentColor = Color.White,
                actionIconContentColor = Color.White,
            ),
        /*  actions = {
              IconButton(onClick = { }) {
                  Icon(Icons.Default.Settings, contentDescription = "Settings")
              }
          },*/
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button),
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = null,
                    modifier =
                        Modifier.clickable {
                            onOpenDrawer()
                        },
                )
            }
        },
    )
}
