package com.example.tiendaguauymiau.ui.screens // Asegúrate que el package sea el correcto

// Imports necesarios
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment // Import correcto
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen() {
    // 1. CORRECCIÓN: Usamos mutableStateOf para un solo valor de texto
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // 2. CORRECCIÓN: La sintaxis de Column es Column(...) { ...contenido... }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally // 3. CORRECCIÓN: Typo
    ) {


        Text(
            text = "GUAU&MIAU",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // 4. CORRECCIÓN: Typo, es Spacer
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("CONTRASEÑA") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                // Lógica de login
            },
            modifier = Modifier.fillMaxWidth() // Añadido para que ocupe todo el ancho
        ) {
            Text("Iniciar Sesión") // Nota: "Sesión" lleva tilde
        }

        TextButton(
            onClick = {
                // Lógica para navegar al registro
            }
        ) {
            Text("¿No tienes cuenta? Regístrate")
        }
    }
}