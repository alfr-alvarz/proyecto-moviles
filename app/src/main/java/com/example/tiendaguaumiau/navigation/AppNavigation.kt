package com.example.tiendaguaumiau.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tiendaguaumiau.ui.screens.HomeScreen
import com.example.tiendaguaumiau.ui.screens.LoginScreen
import com.example.tiendaguaumiau.ui.screens.ProfileScreen
import com.example.tiendaguaumiau.ui.screens.RegisterScreen
import com.example.tiendaguaumiau.ui.screens.SettingsScreen
import com.example.tiendaguaumiau.viewmodel.MainViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
        modifier = modifier
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(navController = navController, viewModel = viewModel)
        }
        composable(route = Screen.Profile.route) {
            ProfileScreen(navController = navController, viewModel = viewModel)
        }
        composable(route = Screen.Settings.route) {
            SettingsScreen(navController = navController, viewModel = viewModel)
        }
        composable(route = Screen.Register.route) {
            RegisterScreen(viewModel = viewModel)
        }
        composable(route = Screen.Login.route) {
            LoginScreen(navController = navController, viewModel = viewModel)
        }
    }
}
