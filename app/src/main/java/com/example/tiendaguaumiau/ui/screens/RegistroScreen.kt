package com.example.tiendaguaumiau.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.tiendaguaumiau.data.MascotaData
import com.example.tiendaguaumiau.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(viewModel: MainViewModel) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    val mascotas = remember { mutableStateListOf<MascotaData>() }

    var nombreError by remember { mutableStateOf<String?>(null) }
    var correoError by remember { mutableStateOf<String?>(null) }
    var contrasenaError by remember { mutableStateOf<String?>(null) }
    var mascotasError by remember { mutableStateOf<String?>(null) }

    val errorApi by viewModel.errorState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(errorApi) {
        errorApi?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    fun validateFields(): Boolean {
        nombreError = when {
            nombre.isBlank() -> "El nombre es obligatorio"
            !nombre.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) -> "El nombre solo puede contener letras y espacios"
            nombre.length > 50 -> "El nombre no puede exceder los 50 caracteres"
            else -> null
        }

        correoError = if (!correo.endsWith("@duoc.cl", ignoreCase = true)) "El correo debe ser de dominio @duoc.cl" else null

        val passwordRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$!%*?&])[A-Za-z\\d@#$!%*?&]{8,}$")
        contrasenaError = when {
            !contrasena.matches(passwordRegex) -> "La contraseña debe tener 8 caracteres, mayúscula, minúscula, número y carácter especial."
            contrasena != confirmarContrasena -> "Las contraseñas no coinciden"
            else -> null
        }
        
        mascotasError = if (mascotas.any { it.nombre.isBlank() || it.tipo.isBlank() }) "Todos los campos de las mascotas son obligatorios" else null

        return nombreError == null && correoError == null && contrasenaError == null && mascotasError == null
    }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { paddingValues ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {
            item {
                Text(
                    text = "Registro de usuario",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            item {
                OutlinedTextField(
                    label = { Text("NOMBRE COMPLETO") },
                    value = nombre,
                    onValueChange = { nombre = it },
                    isError = nombreError != null,
                    supportingText = { if (nombreError != null) Text(nombreError!!) },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
            }
            item {
                OutlinedTextField(
                    label = { Text("CORREO ELECTRÓNICO (@duoc.cl)") },
                    value = correo,
                    onValueChange = { correo = it },
                    isError = correoError != null,
                    supportingText = { if (correoError != null) Text(correoError!!) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
            }
            item {
                OutlinedTextField(
                    label = { Text("CONTRASEÑA") },
                    value = contrasena,
                    onValueChange = { contrasena = it },
                    isError = contrasenaError != null,
                    supportingText = { if (contrasenaError != null) Text(contrasenaError!!) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
            }
            item {
                OutlinedTextField(
                    label = { Text("CONFIRMAR CONTRASEÑA") },
                    value = confirmarContrasena,
                    onValueChange = { confirmarContrasena = it },
                    isError = contrasenaError != null,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
            }
            item {
                OutlinedTextField(
                    label = { Text("TELÉFONO (opcional)") },
                    value = telefono,
                    onValueChange = { telefono = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
                )
            }

            item {
                Text(
                    text = "Registro de mascotas",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            if (mascotasError != null) {
                item {
                    Text(
                        mascotasError!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }

            itemsIndexed(mascotas) { index, pet ->
                PetFormItem(
                    petData = pet,
                    onNameChange = { newName -> mascotas[index] = mascotas[index].copy(nombre = newName) },
                    onTypeChange = { newType -> mascotas[index] = mascotas[index].copy(tipo = newType) },
                    onDelete = { mascotas.removeAt(index) }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Button(
                    onClick = { mascotas.add(MascotaData("", "")) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("AÑADIR MASCOTA")
                }
                Spacer(modifier = Modifier.height(32.dp)
                )
            }

            item {
                Button(
                    onClick = { if (validateFields()) { viewModel.register(nombre, correo, contrasena, telefono, mascotas.toList()) } },
                    modifier = Modifier.fillMaxWidth(),
                ) { Text("REGISTRAR", style = MaterialTheme.typography.titleMedium) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PetFormItem(petData: MascotaData, onNameChange: (String) -> Unit, onTypeChange: (String) -> Unit, onDelete: () -> Unit) {
    val petTypes = listOf("Gato", "Perro", "Ave", "Otro")
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.small)
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = petData.nombre,
            onValueChange = onNameChange,
            label = { Text("Nombre de la mascota (máx. 50)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            OutlinedTextField(
                value = petData.tipo.ifEmpty { "Seleccione un tipo" },
                onValueChange = {},
                label = { Text("Tipo de mascota") },
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                petTypes.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type) },
                        onClick = { onTypeChange(type); expanded = false }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onDelete,
            modifier = Modifier.align(Alignment.End),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) { Text("ELIMINAR") }
    }
}