package dev.vijayakumar.userapp.view

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController



@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val bottomNavItems = listOf(
        BottomNavItem("login", "Login", Icons.Default.Person),
        BottomNavItem("favorite", "Favorite", Icons.Default.AccountCircle),
        BottomNavItem("userlist", "Users", Icons.Default.List),
        BottomNavItem("search_by_name", "Search", Icons.Default.Search)
    )

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController, items = bottomNavItems)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("login") {
                LoginScreen(navController = navController)
            }

            composable("favorite") {
                FavoriteScreen(navController = navController)
            }

            composable("userlist") {
                UserListScreen(navController = navController)
            }

            composable("search_by_name") {
                SearchUserScreen(navController = navController)
            }
        }
    }
}
