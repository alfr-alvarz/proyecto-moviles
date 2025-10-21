package com.example.tiendaguaumiau.ui.screens
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tiendaguaumiau.navigation.Screen
import com.example.tiendaguaumiau.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
) {
    val items = listOf(Screen.Home, Screen.Profile)
    var selectedItem by remember { mutableStateOf(1) }

    Scaffold (
        bottomBar = {
            NavigationBar {
                items.forEachIndexed {index, screen ->
                    NavigationBarItem(
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            viewModel.navigateTo(screen)
                        },
                        label = { Text(screen.route)},
                        icon = {
                            Icon(
                                imageVector = if (screen == Screen.Home) Icons.Default.Home else Icons.Default.Person,
                                contentDescription = screen.route,
                            )
                        }
                    )
                }
            }
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("¡Bienvenido al Perfil!")
        }
    }
}