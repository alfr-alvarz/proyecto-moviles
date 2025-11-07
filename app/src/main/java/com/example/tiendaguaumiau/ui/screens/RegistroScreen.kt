package com.example.tiendaguaumiau.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
// Importamos las nuevas clases del archivo del ViewModel
import com.example.tiendaguaumiau.viewmodel.PetData
import com.example.tiendaguaumiau.viewmodel.RegistroViewModel
import com.example.tiendaguaumiau.viewmodel.RegistrationEvent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    vmuser: RegistroViewModel = viewModel()
) {
    val state by vmuser.state.collectAsState()
    val errors by vmuser.errors.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Efecto para eventos del ViewModel
    LaunchedEffect(key1 = true) {
        vmuser.registrationEvent.collectLatest { event ->
            when (event) {
                is RegistrationEvent.Success -> {
                    scope.launch { snackbarHostState.showSnackbar(event.message) }
                }
                is RegistrationEvent.Error -> {
                    scope.launch { snackbarHostState.showSnackbar(event.message) }
                }
                is RegistrationEvent.ValidationFailed -> {
                    scope.launch { snackbarHostState.showSnackbar(event.message) }
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            //REGISTRO DE USUARIO
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
                    value = state.nombre,
                    onValueChange = vmuser::onNombreChange,
                    isError = errors.nombre != null,
                    supportingText = { if (errors.nombre != null) Text(errors.nombre!!) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            }
            item {
                OutlinedTextField(
                    label = { Text("CORREO ELECTRONICO") },
                    value = state.email,
                    onValueChange = vmuser::onEmailChange,
                    isError = errors.email != null,
                    supportingText = { if (errors.email != null) Text(errors.email!!) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            }
            item {
                OutlinedTextField(
                    label = { Text("CONTRASEÑA") },
                    value = state.password,
                    onValueChange = vmuser::onPasswordChange,
                    isError = errors.password != null,
                    supportingText = { if (errors.password != null) Text(errors.password!!) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            }
            item {
                OutlinedTextField(
                    label = { Text("CONFIRMAR CONTRASEÑA") },
                    value = state.confirmPassword,
                    onValueChange = vmuser::onConfirmPasswordChange,
                    isError = errors.confirmPassword != null,
                    supportingText = { if (errors.confirmPassword != null) Text(errors.confirmPassword!!) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            }
            item {
                OutlinedTextField(
                    label = { Text("TELÉFONO (opcional)") },
                    value = state.telefono,
                    onValueChange = vmuser::onTelefonoChange,
                    isError = errors.telefono != null,
                    supportingText = { if (errors.telefono != null) Text(errors.telefono!!) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            // REGISTRO DE MASCOTAS
            item {
                Text(
                    text = "Registro de mascotas",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // LISTA DE MASCOTAS
            itemsIndexed(state.pets) { index, pet ->
                PetFormItem(
                    petData = pet,
                    // Conecta los eventos a las funciones del ViewModel
                    onNameChange = { newName ->
                        vmuser.onPetNameChange(index, newName)
                    },
                    onTypeChange = { newType ->
                        vmuser.onPetTypeChange(index, newType)
                    },
                    onDelete = { vmuser.removePet(index) }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // BOTÓN AÑADIR MASCOTAS
            item {
                Button(
                    onClick = {
                        vmuser.addPet()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("AÑADIR NUEVO REGISTRO")
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            // BOTÓN REGISTRAR
            item {
                Button(
                    onClick = {
                        vmuser.onRegisterClick()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("REGISTRAR", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PetFormItem(
    petData: PetData,
    onNameChange: (String) -> Unit,
    onTypeChange: (String) -> Unit,
    onDelete: () -> Unit
) {

    val petTypes = listOf("Gato", "Perro", "Ave", "Otro")
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Gray, MaterialTheme.shapes.small)
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = petData.name,
            onValueChange = onNameChange,
            label = { Text("Nombre de la mascota") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = petData.type.ifEmpty { "Seleccione un tipo" },
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
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                petTypes.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type) },
                        onClick = {
                            onTypeChange(type)
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onDelete,
            modifier = Modifier.align(Alignment.End),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("ELIMINAR")
        }
    }
}