package dev.vijayakumar.userapp.view

//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(navController: NavController, items: List<BottomNavItem>) {
    BottomNavigation {
        val currentRoute = navController.currentBackStackEntryAsState()?.value?.destination?.route

        items.forEach { item ->
            BottomNavigationItem(
                modifier = Modifier.padding(bottom = 50.dp).background(color = Color.LightGray),
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            // Avoid multiple copies of the same destination
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}


data class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
)