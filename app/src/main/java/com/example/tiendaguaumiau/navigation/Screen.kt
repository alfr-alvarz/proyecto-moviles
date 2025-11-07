package com.example.tiendaguaumiau.navigation

sealed class Screen(val route: String) {

    data object Home : Screen("home_page")

    data object Profile : Screen("profile_page")

    data object Settings : Screen("settings_page")

    data object  Login : Screen("login_page")

    data object Register : Screen ("register_page")

}