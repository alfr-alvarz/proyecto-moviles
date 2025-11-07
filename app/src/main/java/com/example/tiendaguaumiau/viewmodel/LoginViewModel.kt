package com.example.tiendaguaumiau.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendaguaumiau.data.LocalUserDatabase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false // mostrar/ocultar overlay
)

sealed class LoginEvent {
    data class Success(val user: FormState) : LoginEvent()
    data class Error(val message: String) : LoginEvent()
}

class LoginViewModel : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state

    private val _loginEvent = MutableSharedFlow<LoginEvent>()
    val loginEvent = _loginEvent.asSharedFlow()

    fun onEmailChange(v: String) {
        _state.value = _state.value.copy(email = v)
    }

    fun onPasswordChange(v: String) {
        _state.value = _state.value.copy(password = v)
    }

    fun onLoginClick() {
        viewModelScope.launch {
            val s = _state.value

            if (s.email.isBlank() || s.password.isBlank()) {
                _loginEvent.emit(LoginEvent.Error("Email y contraseña no pueden estar vacíos"))
                return@launch
            }

            _state.value = _state.value.copy(isLoading = true)
            delay(3000)

            // Busca al usuario en la base de datos
            val loggedInUser = LocalUserDatabase.login(s.email, s.password)

            if (loggedInUser != null) {
                _loginEvent.emit(LoginEvent.Success(loggedInUser))
                resetForm()
            } else {
                _loginEvent.emit(LoginEvent.Error("Credenciales incorrectas"))
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }

    private fun resetForm() {
        _state.value = LoginState() // Resetea email, pass y isLoading
    }
}