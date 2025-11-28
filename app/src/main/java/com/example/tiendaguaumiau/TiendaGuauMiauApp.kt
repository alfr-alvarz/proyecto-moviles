package com.example.tiendaguaumiau

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.tiendaguaumiau.navigation.AppNavigation
import com.example.tiendaguaumiau.navigation.NavigationEvent
import com.example.tiendaguaumiau.view.theme.TiendaGuauMiauTheme
import com.example.tiendaguaumiau.viewmodel.MainViewModel
import com.example.tiendaguaumiau.viewmodel.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest

@Composable
fun TiendaGuauMiauApp() {
    TiendaGuauMiauTheme {
        val context = LocalContext.current
        // Usamos la Factory para crear el ViewModel con sus dependencias.
        val viewModel: MainViewModel = viewModel(factory = ViewModelFactory(context))
        val navController = rememberNavController()

        LaunchedEffect(key1 = Unit) {
            viewModel.navigationEvents.collectLatest { event ->
                when (event) {
                    is NavigationEvent.NavigateTo -> {
                        navController.navigate(event.route.route) {
                            event.popupToRoute?.let {
                                popUpTo(it.route) {
                                    inclusive = event.inclusive
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
            AppNavigation(
                navController = navController,
                viewModel = viewModel,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}