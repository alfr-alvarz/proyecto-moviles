package com.example.tiendaguauymiau.ui.screens // Asegúrate que el package sea el correcto

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

data class PetData(
    var name: String = "",
    var type: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen() {
    // --- FORMULARIO DE USUARIO ---
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }

    // --- ESTADO LISTA DE MASCOTAS ---

    val pets = remember { mutableStateListOf<PetData>() }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // --- REGISTRO DE USUARIO ---
        item {
            Text(
                text = "Registro de usuario",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        item { UserFormField(label = "NOMBRE COMPLETO", value = nombre, onValueChange = { nombre = it }) }
        item { UserFormField(label = "CORREO ELECTRÓNICO", value = email, onValueChange = { email = it }) }
        item {
            UserFormField(
                label = "CONTRASEÑA",
                value = password,
                onValueChange = { password = it },
                isPassword = true
            )
        }
        item {
            UserFormField(
                label = "CONFIRMAR CONTRASEÑA",
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                isPassword = true
            )
        }
        item { UserFormField(label = "TELÉFONO (opcional)", value = telefono, onValueChange = { telefono = it }) }

        item { Spacer(modifier = Modifier.height(24.dp)) }

        // --- REGISTRO DE MASCOTAS ---
        item {
            Text(
                text = "Registro de mascotas",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // --- LISTA  DE MASCOTAS ---
        // Usamos itemsIndexed para tener el "index" y poder eliminar
        itemsIndexed(pets) { index, pet ->
            PetFormItem(
                petData = pet,
                onDelete = { pets.removeAt(index) }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // --- BOTÓN AÑADIR MASCOTAS ---
        item {
            Button(
                onClick = {
                    // Simplemente añadimos un nuevo objeto vacío a la lista
                    pets.add(PetData())
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("AÑADIR NUEVO REGISTRO")
            }
        }

        item { Spacer(modifier = Modifier.height(32.dp)) }

        // --- BOTÓN FINAL DE REGISTRO ---
        item {
            Button(
                onClick = {
                    // AQUÍ IRÁ LA LÓGICA DE VALIDACIÓN Y REGISTRO
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("REGISTRAR", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

// --- COMPOSABLE
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserFormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        singleLine = true,
        // CORRECCIÓN: Se usa VisualTransformation.None en lugar de PasswordVisualTransformation.None
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None
    )
}

// --- COMPOSABLE AUXILIAR PARA EL FORMULARIO DE CADA MASCOTA ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PetFormItem(
    petData: PetData,
    onDelete: () -> Unit
) {
    // Opciones para el Dropdown
    val petTypes = listOf("Gato", "Perro", "Ave", "Otro")
    var expanded by remember { mutableStateOf(false) }


    var petName by remember { mutableStateOf(petData.name) }
    var petType by remember { mutableStateOf(petData.type) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Gray, MaterialTheme.shapes.small)
            .padding(16.dp)
    ) {
        // --- CAMPO: NOMBRE DE MASCOTA ---
        OutlinedTextField(
            value = petName,
            onValueChange = {
                petName = it
                petData.name = it
            },
            label = { Text("Nombre de la mascota") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))


        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = petType.ifEmpty { "Seleccione un tipo" },
                onValueChange = {},
                label = { Text("Tipo de mascota") },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            // Contenido del Menú
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                petTypes.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type) },
                        onClick = {
                            petType = type
                            petData.type = type // Actualizamos el objeto original
                            expanded = false // Cerramos el menú
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


        Button(
            onClick = onDelete, // Llama a la función que nos pasaron
            modifier = Modifier.align(Alignment.End),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("ELIMINAR")
        }
    }
}