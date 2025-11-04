package com.example.tiendaguaumiau.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendaguaumiau.data.LocalUserDatabase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

data class PetData(
    val name: String = "",
    val type: String = ""
)

data class FormState(
    val nombre: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val telefono: String = "",

    val pets: List<PetData> = emptyList()
)

data class FormErrors(
    val nombre: String? = null,
    val email: String? = null,
    val password: String? = null,
    val confirmPassword: String? = null,
    val telefono: String? = null
)

//Eventos para UI
sealed class RegistrationEvent {
    data class Success(val message: String) : RegistrationEvent()
    data class Error(val message: String) : RegistrationEvent()
    data class ValidationFailed(val message: String) : RegistrationEvent()
}


class RegistroViewModel : ViewModel() {
    private val _state = MutableStateFlow(FormState())
    val state: StateFlow<FormState> = _state

    private val _errors = MutableStateFlow(FormErrors())
    val errors: StateFlow<FormErrors> = _errors

    // Canal para enviar eventos a la UI
    private val _registrationEvent = MutableSharedFlow<RegistrationEvent>()
    val registrationEvent = _registrationEvent.asSharedFlow()


    // Funciones de cambio de estado (Usuario)
    fun onNombreChange(v: String) {
        _state.value = _state.value.copy(nombre = v)
        if (_errors.value.nombre != null) validate() // Re-valida al escribir
    }

    fun onEmailChange(v: String) {
        _state.value = _state.value.copy(email = v)
        if (_errors.value.email != null) validate()
    }

    fun onPasswordChange(v: String) {
        _state.value = _state.value.copy(password = v)
        if (_errors.value.password != null) validate()
    }

    fun onConfirmPasswordChange(v: String) {
        _state.value = _state.value.copy(confirmPassword = v)
        if (_errors.value.confirmPassword != null) validate()
    }

    fun onTelefonoChange(v: String) {
        _state.value = _state.value.copy(telefono = v)
        if (_errors.value.telefono != null) validate()
    }

    // Funciones de cambio de estado (Mascotas)
    fun addPet() {
        val newPets = _state.value.pets + PetData() // Añade una mascota vacía
        _state.value = _state.value.copy(pets = newPets)
    }

    fun removePet(index: Int) {
        val newPets = _state.value.pets.toMutableList().apply {
            removeAt(index)
        }
        _state.value = _state.value.copy(pets = newPets)
    }

    fun onPetNameChange(index: Int, name: String) {
        val newPets = _state.value.pets.toMutableList().apply {
            this[index] = this[index].copy(name = name)
        }
        _state.value = _state.value.copy(pets = newPets)
    }

    fun onPetTypeChange(index: Int, type: String) {
        val newPets = _state.value.pets.toMutableList().apply {
            this[index] = this[index].copy(type = type)
        }
        _state.value = _state.value.copy(pets = newPets)
    }

    // Lógica de Validación y Registro

    // validate() actualiza errores y devuelve un booleano
    private fun validate(): Boolean {
        val s = _state.value
        var ok = true

        val nombreErr = when {
            s.nombre.isBlank() -> "Obligatorio"
            !s.nombre.matches("^[a-zA-Z\\s]+$".toRegex()) -> "Sólo carácteres alfabéticos y espacios"
            s.nombre.length > 50 -> "Maximo de 50 carácteres"
            else -> null
        }
        val emailErr = when {
            !android.util.Patterns.EMAIL_ADDRESS.matcher(s.email).matches() -> "Email inválido"
            !s.email.endsWith("@duoc.cl") -> "Debe ser un correo @duoc.cl"
            else -> null
        }
        val passwordErr = when {
            s.password.length < 8 -> "Mínimo 8 caracteres"
            s.password.none { it.isUpperCase() } -> "Debe contener al menos una mayúscula"
            s.password.none { it.isLowerCase() } -> "Debe contener al menos una minúscula"
            s.password.none { it.isDigit() } -> "Debe contener al menos un número"
            s.password.none { !it.isLetterOrDigit() } -> "Debe contener un carácter especial (ej: @#$%)"
            else -> null
        }

        val confirmPasswordErr =
            if (s.password != s.confirmPassword) "No es la misma contraseña" else null

        val telefonoErr = run {
            var telefonoLimpio = s.telefono.filter { it.isDigit() }
            if (telefonoLimpio.length == 11 && telefonoLimpio.startsWith("56")) {
                telefonoLimpio = telefonoLimpio.substring(2)
            }
            when {
                telefonoLimpio.isEmpty() -> null
                !telefonoLimpio.matches("^9\\d{8}$".toRegex()) -> "Debe ser un móvil válido (9 dígitos, ej: 9xxxxxxxx)"
                else -> null
            }
        }

        _errors.value = FormErrors(nombreErr, emailErr, passwordErr, confirmPasswordErr, telefonoErr)

        // --- Validación de Mascotas ---
        // Comprueba que todas las mascotas tengan nombre y tipo
        val petsValid = s.pets.all { it.name.isNotBlank() && it.type.isNotBlank() }

        ok = listOf(nombreErr, emailErr, passwordErr, confirmPasswordErr, telefonoErr).all { it == null } && petsValid
        return ok
    }

    fun onRegisterClick() {
        if (validate()) {
            viewModelScope.launch {
                try {

                    LocalUserDatabase.register(_state.value)

                    // Si funciona todo
                    _registrationEvent.emit(RegistrationEvent.Success("Registro exitoso"))
                    reset()

                } catch (e: Exception) {
                    // Si el guardado falla
                    _registrationEvent.emit(RegistrationEvent.Error(e.message ?: "Error al registrar"))
                }
            }
        } else {
            // Validación fallida
            viewModelScope.launch {
                val petsValid = _state.value.pets.all { it.name.isNotBlank() && it.type.isNotBlank() }
                val msg = if (!petsValid) "Revisa los datos de tus mascotas" else "Revisa los campos requeridos"
                _registrationEvent.emit(RegistrationEvent.ValidationFailed(msg))
            }
        }
    }

    private fun reset() {
        _state.value = FormState()
        _errors.value = FormErrors()
    }
}