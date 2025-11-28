package com.example.tiendaguaumiau.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendaguaumiau.data.MascotaData
import com.example.tiendaguaumiau.data.UsuarioConMascotas
import com.example.tiendaguaumiau.data.network.MascotaRegistroDto
import com.example.tiendaguaumiau.data.repository.PreferencesRepository
import com.example.tiendaguaumiau.data.repository.UsuarioRepository
import com.example.tiendaguaumiau.navigation.NavigationEvent
import com.example.tiendaguaumiau.navigation.Screen
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val usuarioRepository: UsuarioRepository,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _navigationEvents = MutableSharedFlow<NavigationEvent>()
    val navigationEvents: SharedFlow<NavigationEvent> = _navigationEvents.asSharedFlow()

    val usuariosConMascotas: StateFlow<List<UsuarioConMascotas>> = usuarioRepository.usuariosConMascotas
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState.asStateFlow()

    private val _loggedInUser = MutableStateFlow<UsuarioConMascotas?>(null)
    val loggedInUser: StateFlow<UsuarioConMascotas?> = _loggedInUser.asStateFlow()

    val backgroundImageUri: StateFlow<String?> = preferencesRepository.backgroundImageUri
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        sincronizarDatos()
    }

    fun sincronizarDatos() {
        viewModelScope.launch {
            usuarioRepository.actualizarDatosLocales().onFailure {
                _errorState.value = "Error de sincronización: ${it.message}"
            }
        }
    }

    fun login(correo: String, contrasena: String) {
        viewModelScope.launch {
            val result = usuarioRepository.login(correo, contrasena)
            result.onSuccess {
                _loggedInUser.value = it
                _errorState.value = null
                navigateTo(Screen.Home, popupToRoute = Screen.Login, inclusive = true)
            }.onFailure {
                _errorState.value = it.message
            }
        }
    }

    fun loginComoInvitado() {
        viewModelScope.launch {
            val result = usuarioRepository.loginComoInvitado()
            result.onSuccess {
                _loggedInUser.value = it
                _errorState.value = null
                navigateTo(Screen.Home, popupToRoute = Screen.Login, inclusive = true)
            }.onFailure {
                _errorState.value = "No se pudo encontrar el usuario de ejemplo."
            }
        }
    }

    fun register(nombre: String, correo: String, contrasena: String, telefono: String, mascotas: List<MascotaData>) {
        viewModelScope.launch {
            val mascotasDto = mascotas.map { MascotaRegistroDto(it.nombre, it.tipo) }
            val result = usuarioRepository.registrar(nombre, correo, contrasena, telefono, mascotasDto)
            result.onSuccess {
                _errorState.value = "Registro exitoso. Por favor, inicie sesión."
                navigateTo(Screen.Login, popupToRoute = Screen.Register, inclusive = true)
            }.onFailure {
                _errorState.value = it.message
            }
        }
    }

    fun logout() {
        _loggedInUser.value = null
        navigateTo(Screen.Login, popupToRoute = Screen.Home, inclusive = true, singleTop = true)
    }

    fun saveBackgroundImage(uri: Uri) {
        viewModelScope.launch {
            preferencesRepository.saveBackgroundImageUri(uri.toString())
        }
    }

    fun navigateToRegister() {
        navigateTo(Screen.Register)
    }

    fun navigateToScreen(screen: Screen) {
        navigateTo(screen)
    }

    fun clearError() {
        _errorState.value = null
    }

    private fun navigateTo(screen: Screen, popupToRoute: Screen? = null, inclusive: Boolean = false, singleTop: Boolean = false) {
        viewModelScope.launch { _navigationEvents.emit(NavigationEvent.NavigateTo(screen, popupToRoute, inclusive, singleTop)) }
    }
}