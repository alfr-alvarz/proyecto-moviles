package com.example.tiendaguaumiau.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tiendaguaumiau.navigation.NavigationEvent
import com.example.tiendaguaumiau.navigation.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    // --- NAVEGACIÓN ---
    private val _navigationEvents= MutableSharedFlow<NavigationEvent>()
    val navigationEvents: SharedFlow<NavigationEvent> = _navigationEvents.asSharedFlow()

    // - ESTADO DE SESIÓN -
    // Guarda el estado del usuario que inició sesión
    private val _currentUser = MutableStateFlow<FormState?>(null)
    val currentUser: StateFlow<FormState?> = _currentUser.asStateFlow()

    // --- Funciones de Navegación -
    fun navigateTo(
        screen: Screen,
        popupToRoute: Screen? = null,
        inclusive: Boolean = false,
        singleTop: Boolean = false
    ){
        CoroutineScope(Dispatchers.Main).launch{
            _navigationEvents.emit(NavigationEvent.NavigateTo(
                route = screen,
                popupToRoute = popupToRoute,
                inclusive = inclusive,
                singleTop = singleTop
            ))
        }
    }

    fun navigateBack() {
        CoroutineScope(Dispatchers.Main).launch {
            _navigationEvents.emit(NavigationEvent.PopBackStack)
        }
    }

    fun navigateUp(){
        CoroutineScope(Dispatchers.Main).launch {
            _navigationEvents.emit(NavigationEvent.NavigateUp)
        }
    }

    fun setLoggedInUser(user: FormState) {
        _currentUser.value = user
    }

    /*Limpia el usuario del estado y navega de vuelta al Login.*/
    fun logout() {
        _currentUser.value = null

        // Navegar a login y limpiar historial.
        navigateTo(
            screen = Screen.Login,
            popupToRoute = Screen.Login,
            inclusive = false,
            singleTop = true
        )
    }
}