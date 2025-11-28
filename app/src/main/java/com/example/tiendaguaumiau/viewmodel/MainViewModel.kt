package com.example.tiendaguaumiau.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendaguaumiau.data.AppDatabase
import com.example.tiendaguaumiau.data.Mascota
import com.example.tiendaguaumiau.data.MascotaData
import com.example.tiendaguaumiau.data.Usuario
import com.example.tiendaguaumiau.data.UsuarioConMascotas
import com.example.tiendaguaumiau.navigation.NavigationEvent
import com.example.tiendaguaumiau.navigation.Screen
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val usuarioDao = db.usuarioDao()
    private val mascotaDao = db.mascotaDao()

    private val _navigationEvents = MutableSharedFlow<NavigationEvent>()
    val navigationEvents: SharedFlow<NavigationEvent> = _navigationEvents.asSharedFlow()

    private val _currentUser = MutableStateFlow<UsuarioConMascotas?>(null)
    val currentUser: StateFlow<UsuarioConMascotas?> = _currentUser.asStateFlow()

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState.asStateFlow()

    fun login(correo: String, contrasena: String) {
        viewModelScope.launch {
            val user = usuarioDao.buscarPorCorreoYContrasena(correo, contrasena)
            if (user != null) {
                _currentUser.value = usuarioDao.getUsuarioConMascotas(user.id)
                _errorState.value = null
                navigateTo(Screen.Home, popupToRoute = Screen.Login, inclusive = true)
            } else {
                _errorState.value = "Correo o contraseña incorrectos"
            }
        }
    }

    fun register(nombre: String, correo: String, contrasena: String, telefono: String, mascotas: List<MascotaData>) {
        viewModelScope.launch {
            if (usuarioDao.buscarPorCorreo(correo) != null) {
                _errorState.value = "El correo ya está registrado"
                return@launch
            }

            val nuevoUsuario = Usuario(nombre = nombre, correo = correo, contrasena = contrasena, telefono = telefono)
            val nuevoUsuarioId = usuarioDao.insertar(nuevoUsuario)

            if (mascotas.isNotEmpty()) {
                val mascotasAGuardar = mascotas.map {
                    Mascota(nombre = it.nombre, tipo = it.tipo, idUsuario = nuevoUsuarioId.toInt())
                }
                mascotaDao.insertarVarias(mascotasAGuardar)
            }

            _errorState.value = null
            navigateTo(Screen.Login, popupToRoute = Screen.Register, inclusive = true)
        }
    }

    fun logout() {
        _currentUser.value = null
        navigateTo(Screen.Login, popupToRoute = Screen.Home, inclusive = true, singleTop = true)
    }

    fun clearError() {
        _errorState.value = null
    }

    fun navigateTo(screen: Screen, popupToRoute: Screen? = null, inclusive: Boolean = false, singleTop: Boolean = false) {
        viewModelScope.launch {
            _navigationEvents.emit(NavigationEvent.NavigateTo(screen, popupToRoute, inclusive, singleTop))
        }
    }

    fun navigateBack() {
        viewModelScope.launch { _navigationEvents.emit(NavigationEvent.PopBackStack) }
    }

    fun navigateUp() {
        viewModelScope.launch { _navigationEvents.emit(NavigationEvent.NavigateUp) }
    }
}