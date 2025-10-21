package com.example.tiendaguaumiau

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.LaunchedEffect

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tiendaguaumiau.navigation.NavigationEvent
import com.example.tiendaguaumiau.navigation.Screen
import com.example.tiendaguaumiau.ui.screens.HomeScreen
import com.example.tiendaguaumiau.ui.screens.ProfileScreen
import com.example.tiendaguaumiau.ui.screens.SettingsScreen
import com.example.tiendaguaumiau.view.theme.TiendaGuauMiauTheme
import com.example.tiendaguaumiau.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            TiendaGuauMiauTheme {

                val viewModel: MainViewModel = viewModel()
                val navController = rememberNavController()

                LaunchedEffect(key1 = Unit) {
                    viewModel.navigationEvents.collectLatest { event ->
                        when (event){
                            is NavigationEvent.NavigateTo -> {
                                navController.navigate(event.route.route){
                                    event.popupToRoute?.let {
                                        popUpTo(it.route){
                                            inclusive=event.inclusive
                                        }
                                    }
                                    launchSingleTop = event.singleTop
                                    restoreState = true
                                }
                            }

                            is NavigationEvent.PopBackStack -> navController.popBackStack()
                            is NavigationEvent.NavigateUp -> navController.navigateUp()
                        }
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(route = Screen.Home.route){
                            HomeScreen(navController = navController,viewModel = viewModel)
                        }
                        composable(route= Screen.Profile.route){
                            ProfileScreen(navController = navController, viewModel=viewModel)
                        }
                        composable(route = Screen.Settings.route){
                            SettingsScreen(navController = navController, viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}

/*
@Preview(name="Compact", widthDp = 360, heightDp = 800)
@Composable
fun PreviewCompact(){
    HomeScreenCompacta()
}
*/