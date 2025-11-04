package com.example.tiendaguaumiau.data

import com.example.tiendaguaumiau.viewmodel.FormState
import com.example.tiendaguaumiau.viewmodel.PetData // <-- Añade esta importación

/*Base de datos solo de memoria */
object LocalUserDatabase {

    // --- INICIO: USUARIO DE PRUEBA ---
    private val testUser = FormState(
        nombre = "Usuario de Prueba",
        email = "test@duoc.cl",
        password = "Password123!",
        confirmPassword = "Password123!",
        telefono = "987654321",
        pets = listOf(PetData(name = "Firulais", type = "Perro"))
    )
    // --- FIN: USUARIO DE PRUEBA ---

    // Inicializado con user de prueba
    private val _users = mutableListOf<FormState>(testUser)

    fun register(formState: FormState) {
        val emailExists = _users.any { it.email.equals(formState.email, ignoreCase = true) }

        if (emailExists) {

            throw Exception("El correo electrónico ya está registrado.")
        }
        _users.add(formState)
    }

    fun login(email: String, password: String): FormState? {
        return _users.find { it.email == email && it.password == password }
    }
}